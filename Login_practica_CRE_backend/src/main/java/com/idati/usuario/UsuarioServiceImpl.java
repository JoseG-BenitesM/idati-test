package com.idati.usuario;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository repository;
    
    @Override
    @Transactional
    public UsuarioEntity crearUsuario(UsuarioEntity usuario) {
        return repository.save(usuario);
    }
    
    @Override
    @Transactional
    public UsuarioEntity actualizarUsuario(Integer id, UsuarioEntity usuario) {
        UsuarioEntity existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("UsuarioEntity no encontrado con id " + id));
        
        existente.setUsuarioNombre(usuario.getUsuarioNombre());
        existente.setContrasena(usuario.getContrasena());
        // relaciones se mantienen intactas
        return repository.save(existente);
    }
    
    @Override
    @Transactional
    public void eliminarUsuario(Integer id) {
        UsuarioEntity existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("UsuarioEntity no encontrado con id " + id));
        repository.delete(existente);
    }
    
    @Override
    @Transactional
    public List<UsuarioEntity> listarUsuarios() {
        return repository.findAll();
    }
    
    @Override
    @Transactional
    public UsuarioEntity obtenerUsuarioPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("UsuarioEntity no encontrado con id " + id));
    }
}