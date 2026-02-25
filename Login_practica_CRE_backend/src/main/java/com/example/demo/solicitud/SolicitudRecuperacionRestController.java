package com.example.demo.solicitud;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/solicitudes")
@RequiredArgsConstructor
public class SolicitudRecuperacionRestController {
    
    private final SolicitudRecuperacionService service;
    
    // Usuario solicita recuperación — público
    @PostMapping("/solicitar")
    public ResponseEntity<Map<String, String>> solicitar(@RequestBody Map<String, String> body) {
        try {
            service.solicitarRecuperacion(body.get("correoOUsuario"));
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Solicitud enviada. Espera la aprobación del administrador."
            ));
        } catch (RuntimeException e) {
            if (e.getMessage().equals("YA_TIENE_SOLICITUD_PENDIENTE")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "Ya tienes una solicitud pendiente."));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "No se pudo procesar la solicitud."));
        }
    }
    
    // Admin lista solicitudes pendientes — solo ROLE_ADMIN
    @GetMapping("/pendientes")
    public ResponseEntity<List<SolicitudRecuperacionResponseDTO>> listarPendientes() {
        List<SolicitudRecuperacionResponseDTO> lista = service.listarPendientes();
        if (lista.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(lista);
    }
    // Admin lista todas las solicitudes — historial completo
    @GetMapping
    public ResponseEntity<List<SolicitudRecuperacionResponseDTO>> listarTodas() {
        List<SolicitudRecuperacionResponseDTO> lista = service.listarTodas();
        if (lista.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(lista);
    }
    
    // Admin aprueba solicitud — solo ROLE_ADMIN
    @PatchMapping("/{id}/aprobar")
    public ResponseEntity<Map<String, String>> aprobar(@PathVariable Integer id) {
        try {
            SolicitudRecuperacionEntity solicitud = service.aprobarSolicitud(id);
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Solicitud aprobada. Usuario desbloqueado.",
                    "codigo", solicitud.getCodigo() // en producción esto va por correo
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
    
    // Usuario restablece contraseña con el código — público
    @PostMapping("/restablecer")
    public ResponseEntity<Map<String, String>> restablecer(@RequestBody Map<String, String> body) {
        try {
            String mensaje = service.restablecerContrasena(
                    body.get("codigo"),
                    body.get("nuevaContrasena")
            );
            return ResponseEntity.ok(Map.of("mensaje", mensaje));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Código inválido o ya utilizado."));
        }
    }
}
