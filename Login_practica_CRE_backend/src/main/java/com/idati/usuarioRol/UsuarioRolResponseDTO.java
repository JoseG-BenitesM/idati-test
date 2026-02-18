package com.idati.usuarioRol;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioRolResponseDTO {
    private Integer idUsuario;
    private String nombreUsuario;
    private String rol;
    private String accion;
    private String mensaje;
}
