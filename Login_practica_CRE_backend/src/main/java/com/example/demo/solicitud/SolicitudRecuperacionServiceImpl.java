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
    
    @Override
    @Transactional
    public SolicitudRecuperacionEntity solicitarRecuperacion(String correoOUsuario) {
        // Buscar usuario por correo o nombre
        UsuarioEntity usuario = usuarioRepository.findByCorreoElectronico(correoOUsuario)
                .or(() -> usuarioRepository.findByUsuarioNombre(correoOUsuario))
                .orElseThrow(() -> new RuntimeException("CREDENCIALES_INVALIDAS"));
        
        // Verificar si ya tiene una solicitud pendiente
        List<SolicitudRecuperacionEntity> pendientes = solicitudRepository
                .findByUsuarioIdAndEstado(usuario.getId(), (byte) 0);
        
        if(! pendientes.isEmpty()) {
            throw new RuntimeException("YA_TIENE_SOLICITUD_PENDIENTE");
        }
        
        // Crear nueva solicitud con código placeholder
        // El código real lo genera el admin al aprobar
        SolicitudRecuperacionEntity solicitud = SolicitudRecuperacionEntity.builder()
                .usuario(usuario)
                .codigo("000000") // placeholder hasta que admin apruebe
                .estado((byte) 0) // pendiente
                .build();
        
        return solicitudRepository.save(solicitud);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SolicitudRecuperacionResponseDTO> listarPendientes() {
        return solicitudRepository.findByEstado((byte) 0)
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
        return solicitudRepository.findAll()
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
    @Transactional
    public SolicitudRecuperacionEntity aprobarSolicitud(Integer idSolicitud) {
        SolicitudRecuperacionEntity solicitud = solicitudRepository.findById(idSolicitud)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        
        if(solicitud.getEstado() != 0) {
            throw new RuntimeException("La solicitud ya fue procesada");
        }
        
        // Generar código de 6 dígitos
        String codigo = String.format("%06d", new Random().nextInt(999999));
        
        // Actualizar solicitud
        solicitud.setCodigo(codigo);
        solicitud.setEstado((byte) 1); // aprobada
        solicitudRepository.save(solicitud);
        
        // Desbloquear usuario y resetear intentos
        UsuarioEntity usuario = solicitud.getUsuario();
        usuario.setEstadoUsuario((byte) 1);
        usuario.setIntentosFallidos((byte) 0);
        usuario.setFechaUltimoIntento(null);
        usuarioRepository.save(usuario);
        
        // En producción aquí se enviaría el código por correo
        // Por ahora lo devolvemos en la respuesta para pruebas
        return solicitud;
    }
    
    @Override
    @Transactional
    public String restablecerContrasena(String codigo, String nuevaContrasena) {
        SolicitudRecuperacionEntity solicitud = solicitudRepository
                .findByCodigoAndEstado(codigo, (byte) 1) // estado=1 aprobada
                .orElseThrow(() -> new RuntimeException("CODIGO_INVALIDO"));
        
        // Actualizar contraseña
        UsuarioEntity usuario = solicitud.getUsuario();
        usuario.setContrasena(passwordEncoder.encode(nuevaContrasena));
        usuarioRepository.save(usuario);
        
        // Marcar solicitud como usada
        solicitud.setEstado((byte) 2);
        solicitud.setFechaUso(LocalDateTime.now());
        solicitudRepository.save(solicitud);
        
        return "Contraseña restablecida correctamente";
    }
}
