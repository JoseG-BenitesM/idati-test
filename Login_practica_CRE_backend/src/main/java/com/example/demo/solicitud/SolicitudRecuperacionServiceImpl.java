package com.example.demo.solicitud;

import com.example.demo.usuario.UsuarioEntity;
import com.example.demo.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class SolicitudRecuperacionServiceImpl implements SolicitudRecuperacionService {
    
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
        
        SolicitudRecuperacionEntity solicitud = solicitudRepository.save(SolicitudRecuperacionEntity.builder().usuario(usuario).codigo(codigo).estado((byte) 1).build());
        // Guardar ANTES de enviar correo
        // Si el correo falla la solicitud ya quedó guardada
        
        try {
            String cuerpo = "Solicitud de recuperación de contraseña.\n" + "Ingresa a http://localhost:4200/Restablecer\n" + "Código de recuperación: " + codigo;
            
            servicioCorreo.enviarCorreo(usuario.getCorreoElectronico(), "RECUPERACIÓN DE CONTRASEÑA", cuerpo);
            
        } catch (Exception e) {
            // El correo falló pero la solicitud ya está guardada
            // El admin puede ver el código en /pendientes si es necesario
            System.err.println("Error enviando correo: " + e.getMessage());
        }
        
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
    public SolicitudRecuperacionEntity aprobarSolicitud(Integer idSolicitud) {
        SolicitudRecuperacionEntity solicitud = solicitudRepository.findById(idSolicitud).orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        
        if(solicitud.getEstado() != 0) {
            throw new RuntimeException("La solicitud ya fue procesada");
        }
        
        // Generar código de 6 dígitos
        String codigo = String.format("%06d", new Random().nextInt(999999));
        
        // Actualizar solicitud
        solicitud.setCodigo(codigo); solicitud.setEstado((byte) 1); // aprobada
        solicitudRepository.save(solicitud);
        
        // Desbloquear usuario y resetear intentos
        UsuarioEntity usuario = solicitud.getUsuario(); usuario.setEstadoUsuario((byte) 1);
        usuario.setIntentosFallidos((byte) 0); usuario.setFechaUltimoIntento(null); usuarioRepository.save(usuario);
        
        //Se enviaría el código por correo
        String cuerpoC = "Solicitud de restablecimiento APROBADA.\nporfavor ingrese a Restablecer Contraseña para para reconfigurar tu clave" + "\nCodigo de recuperacion: " + solicitud.getCodigo();
        try {
            servicioCorreo.enviarCorreo(usuario.getCorreoElectronico(), "RECUPERACIÓN DE CONTRASEÑA", cuerpoC);
        } catch (Exception e) {
            System.err.println("Error enviando correo: " + e.getMessage());
        }
        // Por ahora lo devolvemos en la respuesta para pruebas
        return solicitud;
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
