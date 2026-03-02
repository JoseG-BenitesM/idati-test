package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LoginPracticaCreBackendApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(LoginPracticaCreBackendApplication.class, args);
        
        // SpringApplication.run() arranca todo el contexto de Spring:
        // 1. Lee application.properties
        // 2. Conecta a la base de datos
        // 3. Escanea y registra todos los Beans
        // 4. Levanta el servidor Tomcat en el puerto 8080
        // 5. Deja la aplicación escuchando requests HTTP
    }
    
}
