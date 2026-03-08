package com.example.demo.solicitud;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SolicitudRecuperacionRepository extends JpaRepository<SolicitudRecuperacionEntity, Integer> {
    
    // Buscar solicitudes pendientes de un usuario
    List<SolicitudRecuperacionEntity> findByUsuarioIdAndEstado(Integer idUsuario, Byte estado);
    
    // Buscar por código para cuando el usuario restablece
    Optional<SolicitudRecuperacionEntity> findByCodigoAndEstado(String codigo, Byte estado);
    
    // Listar todas las pendientes para que el admin las vea
    List<SolicitudRecuperacionEntity> findByEstado(Byte estado);
    
    List<SolicitudRecuperacionEntity> findByEstadoIn(List<Byte> estados);
    
    long countByUsuarioIdAndFechaSolicitudAfter(
            Integer idUsuario, LocalDateTime fecha);
}
