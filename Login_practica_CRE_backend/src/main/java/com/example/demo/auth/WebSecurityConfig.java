package com.example.demo.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.example.demo.usuario.UsuarioRepository;
import org.springframework.security.config.Customizer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UsuarioRepository usuarioRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable()) // Seguimos deshabilitando CSRF para evitar bloqueos en POST
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll() // Login y Registro libres
                .anyRequest().authenticated()           // El resto pide login
            )
            // --- AQUÍ RECUPERAMOS EL FORMULARIO ---
            .formLogin(Customizer.withDefaults()) 
            // --------------------------------------
            
            // Cambiamos a ALWAYS o IF_REQUIRED para que el formulario pueda guardar tu sesión en el navegador
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            )
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> usuarioRepository.findByCorreoElectronico(username)
                .map(u -> org.springframework.security.core.userdetails.User.builder()
                    .username(u.getCorreoElectronico())
                    .password(u.getContrasena()) // La contraseña en la DB debe estar encriptada
                    .roles("USER")
                    .build())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}