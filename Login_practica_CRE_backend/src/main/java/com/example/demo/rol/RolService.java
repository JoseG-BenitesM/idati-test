package com.example.demo.rol;

import java.util.List;

public interface RolService {
    RolEntity crearRol(RolEntity rol);
    RolEntity actualizarRol(Integer id, RolEntity rol);
    void eliminarRol(Integer id);
    RolEntity obtenerPorId(Integer id);
    List<RolEntity> listarRoles();
}