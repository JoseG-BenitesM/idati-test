package com.example.demo.usuario;

import java.util.List;

public interface UsuarioService {
    // Lo que usamos
    List<UsuarioEntity> listarUsuarios();
    UsuarioEntity actualizarUsuario(Integer id, UsuarioEntity usuario);
    UsuarioEntity obtenerUsuarioPorId(Integer id);

    // Lo que no usamos pero igual lo pongo pq no sé si afectará o no
    UsuarioEntity crearUsuario(UsuarioEntity usuario);
    void eliminarUsuario(Integer id);
    
    // RF-07 / RF-08
    UsuarioEntity bloquearUsuario(Integer id);
    UsuarioEntity desbloquearUsuario(Integer id);
}
