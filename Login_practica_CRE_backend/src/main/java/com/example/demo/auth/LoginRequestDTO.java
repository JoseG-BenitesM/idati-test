package com.example.demo.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {
    
    // puedes dejarlo igual o llamarlo "identificador"
    // Sirve tanto para correo como para nombre de usuario
    @NotBlank(message = "El usuario o correo es obligatorio")
    String correoElectronico;
    
    @NotBlank(message = "La contraseña es obligatoria")
    String contrasena;
}
