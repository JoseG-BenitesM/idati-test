package com.idati.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {
    
    Optional<UsuarioEntity> findByUsuarioNombre(String usuarioNombre);
    
    @Query("SELECT u FROM UsuarioEntity u WHERE u.usuarioNombre = :usuarioNombre AND u.estado_usuario = true")
        
    Optional<UsuarioEntity> findByUsuarioNombreAndEstadoActivo(@Param("usuarioNombre") String usuarioNombre);
    // Optional<UsuarioEntity> findByTokenRecuperacion(String tokenRecuperacion);
    boolean existsByUsuarioNombre(String usuarioNombre);
    // boolean existsByCorreoElectronico(String correoElectronico);
}
