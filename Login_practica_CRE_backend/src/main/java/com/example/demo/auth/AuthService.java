package com.example.demo.auth;

import com.example.demo.usuario.UsuarioEntity;
import com.example.demo.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
// Marca esta clase como componente de lógica de negocio.
// Spring la registra como Bean y la inyecta donde se necesite.
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // límite definido
    private static final int MAX_INTENTOS = 3;

    public Map<String, String> login(LoginRequestDTO request) {

        // 1. Buscar usuario por correo o por nombre de usuario
        UsuarioEntity usuario = usuarioRepository
                // Intenta buscar por correo primero
                .findByCorreoElectronico(request.getCorreoElectronico())
                // Si no encontró por correo, intenta por nombre.
                .or(() -> usuarioRepository.findByUsuarioNombre(request.getCorreoElectronico()))
                // Si ninguno encontró al usuario lanza excepción.
                .orElseThrow(() -> new RuntimeException("CREDENCIALES_INVALIDAS"));

        // 2. Verificar expiración de intentos fallidos (24 horas)
        if (usuario.getIntentosFallidos() > 0
                && usuario.getFechaUltimoIntento() != null) {

            // fecha y hora actual resta 24 horas para obtener el límite
            LocalDateTime hace24horas = LocalDateTime.now().minusHours(24);
            if (usuario.getFechaUltimoIntento().isBefore(hace24horas)) {
                usuario.setIntentosFallidos((byte) 0);
                usuario.setFechaUltimoIntento(null);
                usuarioRepository.save(usuario);
            }
        }

        // 3. Verificar si está bloqueado
        if (!usuario.isActivo()) {

            // AuthController captura esto y devuelve HTTP 423
            throw new RuntimeException("CUENTA_BLOQUEADA");
        }

        // 4. Verificar contraseña
        if (passwordEncoder.matches(
                request.getContrasena(), usuario.getContrasena())) {

            // Login exitoso → resetear contadores
            // (byte) → cast necesario porque el campo es Byte, no Integer
            usuario.setIntentosFallidos((byte) 0);

            usuario.setFechaUltimoIntento(null);

            usuarioRepository.save(usuario);
            // .save() hace UPDATE porque el usuario ya tiene ID

            // Obtener rol real desde la relación
            String rol = usuario.getUsuarioRoles().stream()
                    .findFirst()
                    .map(ur -> ur.getRol().getRolNombre())
                    // Si no tiene rol asignado usa ROLE_EMPLEADO por defecto
                    .orElse("ROLE_EMPLEADO");

            String token = jwtService.generarToken(
                    usuario.getCorreoElectronico(), rol);
            // Genera el JWT con el correo como Subject y el rol como claim

            return Map.of(
                    "token", token,
                    "usuario", usuario.getUsuarioNombre(),
                    "rol", rol
            );

        } else {
            // Login fallido — incrementar intentos
            byte intentos = (byte) (usuario.getIntentosFallidos() + 1);
            // Suma 1 al valor actual y castea a byte

            usuario.setIntentosFallidos(intentos);

            // Registra el momento exacto del intento fallido
            usuario.setFechaUltimoIntento(LocalDateTime.now());

            if (intentos >= MAX_INTENTOS) {

                // Si alcanzó el límite → bloquear cuenta
                usuario.setEstadoUsuario((byte) 0);
                usuarioRepository.save(usuario);
                throw new RuntimeException("CUENTA_BLOQUEADA");
            }

            usuarioRepository.save(usuario);
            throw new RuntimeException("CREDENCIALES_INVALIDAS");
            // Mismo mensaje que usuario no encontrado
            // para no revelar información al atacante
        }
    }
}
