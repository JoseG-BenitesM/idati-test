package com.idati.usuario;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    // Inyección por constructor (mejor práctica)
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<UsuarioEntity> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<UsuarioEntity> obtenerUsuarioPorId(Integer id) {
        return usuarioRepository.findById(id);
    }
}
