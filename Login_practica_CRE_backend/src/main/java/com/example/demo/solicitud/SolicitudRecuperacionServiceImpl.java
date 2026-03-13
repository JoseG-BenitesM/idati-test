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
        
        UsuarioEntity usuario = usuarioRepository
                .findByCorreoElectronico(correoOUsuario)
                .or(() -> usuarioRepository.findByUsuarioNombre(correoOUsuario))
                .orElseThrow(() -> new RuntimeException("CREDENCIALES_INVALIDAS"));
        
        // 1. Verificar límite de 3 solicitudes en la última hora (anti-spam)
        LocalDateTime haceUnaHora = LocalDateTime.now().minusHours(1);
        long solicitudesRecientes = solicitudRepository
                .countByUsuarioIdAndFechaSolicitudAfter(usuario.getId(), (byte) 2, haceUnaHora);
        
        if(solicitudesRecientes >= 3) {
            throw new RuntimeException("LIMITE_SOLICITUDES_ALCANZADO");
        }
        
        // 2. Verificar solicitud pendiente de aprobación admin (usuario bloqueado)
        List<SolicitudRecuperacionEntity> pendientes = solicitudRepository
                .findByUsuarioIdAndEstado(usuario.getId(), (byte) 0);
        if(! pendientes.isEmpty()) {
            throw new RuntimeException("YA_TIENE_SOLICITUD_PENDIENTE");
        }
        
        // 3. FLUJO 1: Usuario bloqueado → crea solicitud, espera aprobación admin
        if(! usuario.isActivo()) {
            return solicitudRepository.save(
                    SolicitudRecuperacionEntity.builder()
                            .usuario(usuario)
                            .codigo("000000")
                            .estado((byte) 0)
                            .build()
            );
        }
        
        // 4. Invalidar códigos activos anteriores
        // el código anterior expira y solo el nuevo es válido
        solicitudRepository.findByUsuarioIdAndEstado(usuario.getId(), (byte) 1)
                .forEach(s -> {
                    s.setEstado((byte) 3); solicitudRepository.save(s);
                });
        solicitudRepository.findByUsuarioIdAndEstado(usuario.getId(), (byte) 4)
                .forEach(s -> {
                    s.setEstado((byte) 3); solicitudRepository.save(s);
                });
        
        // 5. FLUJO 2: Usuario activo → generar nuevo código inmediatamente
        String codigo = String.format("%06d", new Random().nextInt(999999));
        
        SolicitudRecuperacionEntity solicitud = solicitudRepository.save(
                SolicitudRecuperacionEntity.builder()
                        .usuario(usuario)
                        .codigo(codigo)
                        .estado((byte) 1)
                        .build()
        );
        
        // 6. Intentar enviar código por correo — si falla la solicitud queda guardada
        boolean correoEnviado = true;
        try {
            String cuerpo = "Solicitud de recuperación de contraseña.\n"
                    + "Código de recuperación: " + codigo
                    + "\nEste código expira en 5 minutos.";
            servicioCorreo.enviarCorreo(
                    usuario.getCorreoElectronico(),
                    "RECUPERACIÓN DE CONTRASEÑA",
                    cuerpo);
        } catch (Exception e) {
            correoEnviado = false;
            log.error("Error enviando correo a {}: {}",
                    usuario.getCorreoElectronico(), e.getMessage());
        }
        
        // estado=1 correo enviado, estado=4 correo falló pero código válido
        solicitud.setEstado(correoEnviado ? (byte) 1 : (byte) 4);
        solicitudRepository.save(solicitud);
        
        return solicitud;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SolicitudRecuperacionResponseDTO> listarPendientes() {
        return solicitudRepository
                .findByEstadoIn(List.of((byte) 0, (byte) 4))
                .stream()
                .map(s -> SolicitudRecuperacionResponseDTO.builder()
                        .id(s.getId())
                        .idUsuario(s.getUsuario().getId())
                        .nombreUsuario(s.getUsuario().getUsuarioNombre())
                        .correoUsuario(s.getUsuario().getCorreoElectronico())
                        .codigo(s.getCodigo())
                        .fechaSolicitud(s.getFechaSolicitud())
                        .fechaUso(s.getFechaUso())
                        .estado(s.getEstado())
                        .build())
                .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SolicitudRecuperacionResponseDTO> listarTodas() {
        return solicitudRepository
                .findAll().stream().map(s -> SolicitudRecuperacionResponseDTO.builder()
                        .id(s.getId())
                        .idUsuario(s.getUsuario().getId())
                        .nombreUsuario(s.getUsuario().getUsuarioNombre())
                        .correoUsuario(s.getUsuario().getCorreoElectronico())
                        .codigo(s.getCodigo())
                        .fechaSolicitud(s.getFechaSolicitud())
                        .fechaUso(s.getFechaUso())
                        .estado(s.getEstado()).build()).collect(java.util.stream.Collectors.toList());
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
        SolicitudRecuperacionEntity solicitud = solicitudRepository
                .findByCodigoAndEstado(codigo, (byte) 1)
                .or(() -> solicitudRepository.findByCodigoAndEstado(codigo, (byte) 4))
                .orElseThrow(() -> new RuntimeException("CODIGO_INVALIDO"));
        
        // Verificar que el código no haya expirado (5 minutos)
        if(solicitud.getFechaSolicitud().isBefore(
                LocalDateTime.now().minusMinutes(5))) {
            solicitud.setEstado((byte) 3); // expirada
            solicitudRepository.save(solicitud);
            throw new RuntimeException("CODIGO_EXPIRADO");
        }
        if(! solicitud.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("CODIGO_INVALIDO"); // el código no es de este usuario
        }
        // Actualizar contraseña
        usuario.setContrasena(passwordEncoder.encode(nuevaContrasena)); usuarioRepository.save(usuario);
        
        // Marcar solicitud como usada
        solicitud.setEstado((byte) 2); solicitud.setFechaUso(LocalDateTime.now()); solicitudRepository.save(solicitud);
        
        return "Contraseña restablecida correctamente";
    }
    
    // En SolicitudRecuperacionServiceImpl
    @Override
    @Transactional
    public void reenviarCodigo(Integer idSolicitud) {
        SolicitudRecuperacionEntity solicitud = solicitudRepository
                .findById(idSolicitud)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        
        if(solicitud.getEstado() != 4) {
            throw new RuntimeException("La solicitud no requiere reenvío");
        }
        
        // Verificar que no haya expirado (5 minutos)
        if(solicitud.getFechaSolicitud().isBefore(
                LocalDateTime.now().minusMinutes(5))) {
            solicitud.setEstado((byte) 3); // expirada
            solicitudRepository.save(solicitud);
            throw new RuntimeException("CODIGO_EXPIRADO");
        }
        
        // Intentar reenviar — si falla el código sigue válido en estado=4
        try {
            String cuerpo = "Reenvío de código de recuperación.\n"
                    + "Código: " + solicitud.getCodigo()
                    + "\nEste código expira en 5 minutos.";
            servicioCorreo.enviarCorreo(
                    solicitud.getUsuario().getCorreoElectronico(),
                    "RECUPERACIÓN DE CONTRASEÑA - REENVÍO",
                    cuerpo);
            // Correo exitoso → estado=1
            solicitud.setEstado((byte) 1);
            solicitudRepository.save(solicitud);
            log.info("Correo reenviado a {}",
                    solicitud.getUsuario().getCorreoElectronico());
            
        } catch (Exception e) {
            // Correo falló → se mantiene estado=4, no se cae
            log.error("Error reenviando correo a {}: {}",
                    solicitud.getUsuario().getCorreoElectronico(),
                    e.getMessage());
        }
    }
}
