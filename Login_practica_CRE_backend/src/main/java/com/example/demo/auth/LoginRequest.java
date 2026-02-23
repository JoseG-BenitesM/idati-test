package com.example.demo.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    
    // puedes dejarlo igual o llamarlo "identificador"
    @NotBlank(message = "El usuario o correo es obligatorio")
    String correoElectronico;
    
    @NotBlank(message = "La contraseña es obligatoria")
    String contrasena;
}
