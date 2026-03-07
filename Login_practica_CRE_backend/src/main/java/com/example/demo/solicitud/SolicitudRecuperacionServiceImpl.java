package com.example.demo.solicitud;

import com.example.demo.usuario.UsuarioEntity;
import com.example.demo.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class SolicitudRecuperacionServiceImpl implements SolicitudRecuperacionService {
    private static final Logger log =
            LoggerFactory.getLogger(SolicitudRecuperacionServiceImpl.class);
    private final SolicitudRecuperacionRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final CorreoService servicioCorreo;
    
    @Override
    @Transactional
    public SolicitudRecuperacionEntity solicitarRecuperacion(String correoOUsuario) {
        
        UsuarioEntity usuario = usuarioRepository.findByCorreoElectronico(correoOUsuario).or(() -> usuarioRepository.findByUsuarioNombre(correoOUsuario)).orElseThrow(() -> new RuntimeException("CREDENCIALES_INVALIDAS"));
        
        List<SolicitudRecuperacionEntity> pendientes = solicitudRepository.findByUsuarioIdAndEstado(usuario.getId(), (byte) 0);
        
        if(! pendientes.isEmpty()) {
            throw new RuntimeException("YA_TIENE_SOLICITUD_PENDIENTE");
        }
        
        if(! usuario.isActivo()) {
            return solicitudRepository.save(SolicitudRecuperacionEntity.builder().usuario(usuario).codigo("000000").estado((byte) 0).build());
        }
        
        String codigo = String.format("%06d", new Random().nextInt(999999));
        
        SolicitudRecuperacionEntity solicitud = solicitudRepository.save(
                SolicitudRecuperacionEntity.builder()
                        .usuario(usuario)
                        .codigo(codigo)
                        .estado((byte) 1)
                        .build()
        );
        
        // Intentar enviar correo pero no caerse si falla
        boolean correoEnviado = true;
        try {
            String cuerpo = "Solicitud de recuperación de contraseña.\n"
                    + "Código de recuperación: " + codigo;
            servicioCorreo.enviarCorreo(
                    usuario.getCorreoElectronico(),
                    "RECUPERACIÓN DE CONTRASEÑA",
                    cuerpo);
        } catch (Exception e) {
            correoEnviado = false;
            log.error("Error enviando correo a {}: {}",
                    usuario.getCorreoElectronico(), e.getMessage());
        }
        // Guardar el estado del correo en la solicitud para que
        // el controller sepa qué mensaje mostrar
        solicitud.setEstado(correoEnviado ? (byte) 1 : (byte) 4);
        // 4 = aprobada pero correo fallido
        solicitudRepository.save(solicitud);
        
        return solicitud;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SolicitudRecuperacionResponseDTO> listarPendientes() {
        return solicitudRepository.findByEstado((byte) 0).stream().map(s -> SolicitudRecuperacionResponseDTO.builder().id(s.getId()).idUsuario(s.getUsuario().getId()).nombreUsuario(s.getUsuario().getUsuarioNombre()).correoUsuario(s.getUsuario().getCorreoElectronico()).codigo(s.getCodigo()).fechaSolicitud(s.getFechaSolicitud()).fechaUso(s.getFechaUso()).estado(s.getEstado()).build()).collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SolicitudRecuperacionResponseDTO> listarTodas() {
        return solicitudRepository.findAll().stream().map(s -> SolicitudRecuperacionResponseDTO.builder().id(s.getId()).idUsuario(s.getUsuario().getId()).nombreUsuario(s.getUsuario().getUsuarioNombre()).correoUsuario(s.getUsuario().getCorreoElectronico()).codigo(s.getCodigo()).fechaSolicitud(s.getFechaSolicitud()).fechaUso(s.getFechaUso()).estado(s.getEstado()).build()).collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    @Transactional
    public Map<String, String> aprobarSolicitud(Integer idSolicitud) {
        SolicitudRecuperacionEntity solicitud = solicitudRepository.findById(idSolicitud)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        
        if(solicitud.getEstado() != 0) {
            throw new RuntimeException("La solicitud ya fue procesada");
        }
        
        String codigo = String.format("%06d", new Random().nextInt(999999));
        solicitud.setCodigo(codigo);
        solicitud.setEstado((byte) 1);
        solicitudRepository.save(solicitud);
        
        UsuarioEntity usuario = solicitud.getUsuario();
        usuario.setEstadoUsuario((byte) 1);
        usuario.setIntentosFallidos((byte) 0);
        usuario.setFechaUltimoIntento(null);
        usuarioRepository.save(usuario);
        
        // Intentar enviar correo pero no caerse si falla
        boolean correoEnviado = true;
        try {
            String cuerpoC = "Solicitud APROBADA.\nIngresa a Restablecer Contraseña.\n"
                    + "Código de recuperación: " + codigo;
            servicioCorreo.enviarCorreo(
                    usuario.getCorreoElectronico(),
                    "RECUPERACIÓN DE CONTRASEÑA",
                    cuerpoC);
        } catch (Exception e) {
            correoEnviado = false;
            log.error("Error enviando correo a {}: {}",
                    usuario.getCorreoElectronico(), e.getMessage());
        }
        
        // Devolver código siempre + aviso si el correo falló
        Map<String, String> resultado = new java.util.HashMap<>();
        resultado.put("codigo", codigo);
        resultado.put("usuario", usuario.getUsuarioNombre());
        
        if(correoEnviado) {
            resultado.put("mensaje",
                    "Solicitud aprobada. Usuario desbloqueado. Código enviado al correo.");
        } else {
            resultado.put("mensaje",
                    "Solicitud aprobada. Usuario desbloqueado. " +
                            "El correo no pudo enviarse, entrega el código manualmente.");
        }
        
        return resultado;
    }
    
    @Override
    @Transactional
    public String restablecerContrasena(String correoOUsuario, String codigo, String nuevaContrasena) {
        
        // Buscar usuario
        UsuarioEntity usuario = usuarioRepository.findByCorreoElectronico(correoOUsuario).or(() -> usuarioRepository.findByUsuarioNombre(correoOUsuario)).orElseThrow(() -> new RuntimeException("CREDENCIALES_INVALIDAS"));
        
        // Verificar que el código pertenece a ese usuario
        SolicitudRecuperacionEntity solicitud = solicitudRepository.findByCodigoAndEstado(codigo, (byte) 1).orElseThrow(() -> new RuntimeException("CODIGO_INVALIDO"));
        
        if(! solicitud.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("CODIGO_INVALIDO"); // el código no es de este usuario
        }
        // Actualizar contraseña
        usuario.setContrasena(passwordEncoder.encode(nuevaContrasena)); usuarioRepository.save(usuario);
        
        // Marcar solicitud como usada
        solicitud.setEstado((byte) 2); solicitud.setFechaUso(LocalDateTime.now()); solicitudRepository.save(solicitud);
        
        return "Contraseña restablecida correctamente";
    }
}
