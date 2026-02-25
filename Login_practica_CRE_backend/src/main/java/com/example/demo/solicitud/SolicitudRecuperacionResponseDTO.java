package com.example.demo.solicitud;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudRecuperacionResponseDTO {
    private Integer id;
    private Integer idUsuario;
    private String nombreUsuario;
    private String correoUsuario;
    private String codigo;
    private LocalDateTime fechaSolicitud;
    private LocalDateTime fechaUso;
    private Byte estado;
}
