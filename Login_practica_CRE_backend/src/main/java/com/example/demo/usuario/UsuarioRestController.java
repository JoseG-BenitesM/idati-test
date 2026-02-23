package com.example.demo.usuario;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioRestController {
    private final UsuarioService service;
    
    @PostMapping
    public ResponseEntity<UsuarioEntity> crear(@RequestBody UsuarioEntity request) {
        UsuarioEntity creado = service.crearUsuario(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioEntity> actualizar(
            @PathVariable Integer id,
            @RequestBody UsuarioEntity request
    ) {
        UsuarioEntity actualizado = service.actualizarUsuario(id, request);
        return ResponseEntity.ok(actualizado);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        service.eliminarUsuario(id);
        return ResponseEntity.ok("UsuarioEntity eliminado correctamente");
    }
    
    
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioEntity> obtener(@PathVariable Integer id) {
        UsuarioEntity usuario = service.obtenerUsuarioPorId(id);
        return ResponseEntity.ok(usuario);
    }
    
    // Solo ADMIN puede listar (lo proteges con Spring Security en el SecurityConfig)
    @GetMapping
    public ResponseEntity<List<UsuarioEntity>> listar() {
        List<UsuarioEntity> lista = service.listarUsuarios();
        if(lista.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(lista);
    }
    
    // RF-08: Admin bloquea
    @PatchMapping("/{id}/bloquear")
    public ResponseEntity<UsuarioEntity> bloquear(@PathVariable Integer id) {
        return ResponseEntity.ok(service.bloquearUsuario(id));
    }
    
    // RF-08: Admin desbloquea
    @PatchMapping("/{id}/desbloquear")
    public ResponseEntity<UsuarioEntity> desbloquear(@PathVariable Integer id) {
        return ResponseEntity.ok(service.desbloquearUsuario(id));
    }
}
