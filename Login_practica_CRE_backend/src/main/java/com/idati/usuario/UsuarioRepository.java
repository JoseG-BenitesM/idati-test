package com.idati.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {
    Optional<UsuarioEntity> findByUsuarioNombre(String usuarioNombre);
    Optional<UsuarioEntity> findByCorreoElectronico(String correoElectronico);
    Optional<UsuarioEntity> findByTokenRecuperacion(String tokenRecuperacion);
    boolean existsByUsuarioNombre(String usuarioNombre);
    boolean existsByCorreoElectronico(String correoElectronico);
}
