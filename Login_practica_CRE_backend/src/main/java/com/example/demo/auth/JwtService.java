package com.example.demo.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
    
    // Clave secreta (en producción ponla en application.properties)
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    
    // Key → interfaz de Java Security que representa una clave criptográfica
    // Keys.secretKeyFor() → genera una clave aleatoria segura automáticamente
    // SignatureAlgorithm.HS256 → algoritmo HMAC con SHA-256
    //      HMAC = Hash-based Message Authentication Code
    //      SHA-256 = función hash que produce 256 bits
    
    private static final long EXPIRACION_MS = 1000 * 60 * 60;
    // Tiempo de expiración en milisegundos:
    // 1000ms × 60 = 1 minuto
    // 1000ms × 60 × 60 = 1 hora
    // Después de 1 hora el token deja de ser válido
    // y el usuario debe volver a hacer login
    
    public String generarToken(String correo, String rol) {
        return Jwts.builder()
                // Jwts es la clase principal de la librería jjwt
                // .builder() inicia la construcción del token
                
                // Claims = datos que se guardan DENTRO del token
                .setClaims(Map.of("rol", rol))
                
                .setSubject(correo)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRACION_MS))
                .signWith(SECRET_KEY)
                .compact();
    }
    
    public String extraerCorreo(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    
    public String extraerRol(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("rol", String.class);
    }
    
    public boolean validarToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
