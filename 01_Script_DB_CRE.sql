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
    token_recuperacion VARCHAR(64) NULL,
    fecha_tkn_expiracion DATETIME NULL,
    fecha_alta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_baja DATETIME NULL
);

INSERT INTO usuarios (usuario_nombre, correo_electronico, contrasena) VALUES
('admin_01', 'admin01@empresa.com', '$2a$10$NSgY5kwZkYt8QKUnDpvzGOl0AbIRelb8lh3GwftdkOzxgfARN1KC.'),   -- pwd: admin_123
('usuario_02', 'usuario02@empresa.com', '$2a$10$tdEG.LOb.EUwi6sH9U/VbehoCOtaFXPCy2RUlHPT.JWD5J4wfW/fy'), -- pwd: usuario_123
('usuario_03', 'usuario03@empresa.com', '$2a$10$dD4rOJmMmy/2yILJbiMet.TAefxSWM5U/Y7RsRASsy.aX/bF0gftC'), -- pwd: usuario_456
('usuario_04', 'usuario04@empresa.com', '$2a$10$yBktNscCkRfzx9rsDOab.OyaS4f21aynN6Huy4hqrGbiEEobdhqCq'); -- pwd: usuario_789

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