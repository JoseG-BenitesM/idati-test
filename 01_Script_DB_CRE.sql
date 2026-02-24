-- BASE DE DATOS: capacitacion_CRE
-- Corresponde al registro de usuarios y credenciales

-- 0. Creación de la Base de Datos
DROP DATABASE IF EXISTS capacitacion_CRE;
CREATE DATABASE capacitacion_CRE;
USE capacitacion_CRE;

-- TABLA: usuarios
-- Guarda las credenciales de acceso al sistema para cada persona autorizada.
CREATE TABLE IF NOT EXISTS usuarios (
    id INT PRIMARY KEY AUTO_INCREMENT,
    usuario_nombre VARCHAR(32) UNIQUE NOT NULL,
    correo_electronico VARCHAR(128) UNIQUE NOT NULL,
    contrasena VARCHAR(128) NOT NULL,
    estado_usuario TINYINT NOT NULL DEFAULT 1 CHECK (estado_usuario IN (0,1)),
    intentos_fallidos TINYINT DEFAULT 0,
    fecha_ultimo_intento DATETIME NULL,
    fecha_alta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_baja DATETIME NULL
);

INSERT INTO usuarios (usuario_nombre, correo_electronico, contrasena) VALUES
('admin_01', 'admin01@empresa.com', '$2a$10$k8uxbPU3O0UZ8/bRmRQ2gO1BC6/ibVW7HZZtxiBo3MqDQLwI8dKS6'),   -- pwd: admin_123
('usuario_02', 'usuario02@empresa.com', '$2a$10$trnskNIykzSPfNX0bWqwgOdvrHY3qgrhDxI/0gU7xvp/WJ/ruHkbq'), -- pwd: usuario_123
('usuario_03', 'usuario03@empresa.com', '$2a$10$9gnjv5aggAmyyfV5Pfx0kOd2O1tVIWAaw8YB.pbIRSIZFU6c8NPT6'), -- pwd: usuario_456
('usuario_04', 'usuario04@empresa.com', '$2a$10$x70lEJizN2e9YVI9e3cMzO5iqenB2jlX4d5apHDdC/66v1hj02hhW'); -- pwd: usuario_789

-- TABLA: roles
-- Define los roles asignables a usuarios del sistema.
CREATE TABLE IF NOT EXISTS roles (
    id INT PRIMARY KEY AUTO_INCREMENT,
    rol_nombre VARCHAR(32) NOT NULL UNIQUE,
    descripcion VARCHAR(128) DEFAULT NULL
);

INSERT INTO roles (rol_nombre, descripcion) VALUES
('ROLE_ADMIN', 'Tiene control total del sistema'),
('ROLE_GERENTE', 'Gestiona equipos de trabajo'),
('ROLE_SUPERVISOR', 'Supervisa proyectos'),
('ROLE_EMPLEADO', 'Acceso básico al sistema');

-- TABLA: usuarios_roles
-- Relaciona usuarios con uno o varios roles.
CREATE TABLE IF NOT EXISTS usuarios_roles (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_usuario INT,
    id_rol INT,
    fecha_asignacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_usuario_rol (id_usuario, id_rol)
);

INSERT INTO usuarios_roles (id_usuario, id_rol) VALUES
(1, 1), -- admin_01 → ADMIN
(2, 2), -- usuario_02 → GERENTE
(3, 3), -- usuario_03 → SUPERVISOR
(4, 4); -- usuario_04 → EMPLEADO

ALTER TABLE usuarios_roles
    ADD CONSTRAINT fk_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id) ON DELETE CASCADE,
    ADD CONSTRAINT fk_rol FOREIGN KEY (id_rol) REFERENCES roles(id) ON DELETE CASCADE;

-- Índice para búsquedas rápidas de roles asignados a un usuario específico.
CREATE INDEX idx_usuarios_roles_usuario ON usuarios_roles(id_usuario);

-- TABLA: solicitudes_recuperacion
-- Relaciona las situaciones de recuperaciones hechas por los usuarios
CREATE TABLE IF NOT EXISTS solicitudes_recuperacion (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_usuario INT NOT NULL,
    codigo VARCHAR(6) NOT NULL,
    fecha_solicitud DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_uso DATETIME NULL,
    estado TINYINT DEFAULT 0
    -- 0 = pendiente
    -- 1 = aprobada por admin
    -- 2 = usada por usuario
    -- 3 = expirada
);

ALTER TABLE solicitudes_recuperacion
    ADD CONSTRAINT fk_solicitud_usuario 
        FOREIGN KEY (id_usuario) REFERENCES usuarios(id) ON DELETE CASCADE;