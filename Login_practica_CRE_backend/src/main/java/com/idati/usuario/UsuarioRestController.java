package com.idati.usuario;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioRestController {

    private final UsuarioService usuarioService;

    public UsuarioRestController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // GET: Listar todos los usuarios (para la tabla)
    @GetMapping
    public ResponseEntity<List<UsuarioEntity>> listarUsuarios() {
        List<UsuarioEntity> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    // GET: Obtener usuario por ID (opcional)
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioEntity> obtenerPorId(@PathVariable Integer id) {
        return usuarioService.obtenerUsuarioPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
