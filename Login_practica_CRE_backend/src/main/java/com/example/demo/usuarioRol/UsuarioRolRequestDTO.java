package com.example.demo.usuarioRol;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioRolRequestDTO {
    private String accion;
    private Integer idUsuario;
    private Integer idRol;
}

