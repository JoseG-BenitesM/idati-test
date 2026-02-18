package com.idati.usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    // Lo que usamos
    List<UsuarioEntity> listarUsuarios();
    UsuarioEntity actualizarUsuario(Integer id, UsuarioEntity usuario);
    Optional<UsuarioEntity> obtenerUsuarioPorId(Integer id);

    // Lo que no usamos pero igual lo pongo pq no sé si afectará o no
    UsuarioEntity crearUsuario(UsuarioEntity usuario);
    void eliminarUsuario(Integer id);
}