# Login_practica_CRE

![Java](https://img.shields.io/badge/Java-21-007396?style=flat&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.2-6DB33F?style=flat&logo=springboot)
![Spring Security](https://img.shields.io/badge/Spring_Security-7.0.2-6DB33F?style=flat&logo=springsecurity)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=flat&logo=mysql)
![Angular](https://img.shields.io/badge/Angular-DD0031?style=flat&logo=angular)
![JWT](https://img.shields.io/badge/JWT-000000?style=flat&logo=jsonwebtokens)

Módulo de autenticación standalone — Backend Spring Boot + Frontend Angular.

---

## Stack

| Capa | Tecnología |
|---|---|
| Backend | Java 21, Spring Boot 4.0.2, Spring Security, JWT, Hibernate, Maven |
| Frontend | Angular, TypeScript |
| Base de datos | MySQL 8.0 |

---

## Requisitos

- Java 21+
- Node.js + Angular CLI
- MySQL 8.0+
- Maven

---

## Instalación

### Backend

```bash
git clone https://github.com/tu-usuario/Login_practica_CRE_backend.git
cd Login_practica_CRE_backend
```

Configurar `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/capacitacion_CRE
spring.datasource.username=root
spring.datasource.password=TU_CONTRASEÑA
```

```bash
mvn spring-boot:run
```

### Frontend

```bash
git clone https://github.com/tu-usuario/Login_practica_CRE_frontend.git
cd Login_practica_CRE_frontend
npm install
ng serve
```

---

## Base de datos

Ejecutar el script SQL incluido en el repositorio. Tablas: `usuarios`, `roles`, `usuarios_roles`, `solicitudes_recuperacion`.

---

## Endpoints

### Públicos

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/auth/login` | Iniciar sesión |
| POST | `/api/solicitudes/solicitar` | Solicitar recuperación de contraseña |
| POST | `/api/solicitudes/restablecer` | Restablecer contraseña con código |

### Protegidos — `ROLE_ADMIN`

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/usuarios` | Listar usuarios |
| GET | `/api/usuarios/{id}` | Obtener usuario |
| PATCH | `/api/usuarios/{id}/bloquear` | Bloquear usuario |
| PATCH | `/api/usuarios/{id}/desbloquear` | Desbloquear usuario |
| GET | `/api/solicitudes` | Historial de solicitudes |
| GET | `/api/solicitudes/pendientes` | Solicitudes pendientes |
| PATCH | `/api/solicitudes/{id}/aprobar` | Aprobar solicitud y desbloquear usuario |

---

## Vistas Angular

| Vista | Ruta | Rol |
|---|---|---|
| Login | `/login` | Público |
| Recuperar contraseña | `/recuperar` | Público |
| Restablecer contraseña | `/restablecer` | Público |
| Dashboard | `/admin/dashboard` | ROLE_ADMIN |
| Gestión de usuarios | `/admin/usuarios` | ROLE_ADMIN |
| Solicitudes | `/admin/solicitudes` | ROLE_ADMIN |

---

## Usuarios de prueba (predeterminados)

| Usuario | Correo | Contraseña | Rol |
|---|---|---|---|
| admin_01 | admin01@empresa.com | admin_123 | ROLE_ADMIN |
| usuario_02 | usuario02@empresa.com | usuario_123 | ROLE_GERENTE |
| usuario_03 | usuario03@empresa.com | usuario_456 | ROLE_SUPERVISOR |
| usuario_04 | usuario04@empresa.com | usuario_789 | ROLE_EMPLEADO |

---

## Proyecto académico — CRE Capacitación © 2026
