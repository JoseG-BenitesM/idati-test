package com.idati.usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    List<UsuarioEntity> listarUsuarios();

    Optional<UsuarioEntity> obtenerUsuarioPorId(Integer id);
}
