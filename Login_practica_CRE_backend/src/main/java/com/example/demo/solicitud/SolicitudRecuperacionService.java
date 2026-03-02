package com.example.demo.solicitud;

import java.util.List;

public interface SolicitudRecuperacionService {
    // Usuario solicita recuperación
    SolicitudRecuperacionEntity solicitarRecuperacion(String correoOUsuario);
    
    // Admin ve todas las solicitudes pendientes
    List<SolicitudRecuperacionResponseDTO> listarPendientes();
    
    List<SolicitudRecuperacionResponseDTO> listarTodas();
    
    // Admin aprueba — desbloquea usuario y genera código
    SolicitudRecuperacionEntity aprobarSolicitud(Integer idSolicitud);
    
    // Usuario usa el código para restablecer contraseña
    String restablecerContrasena(String correoOUsuario, String codigo, String nuevaContrasena);
}
