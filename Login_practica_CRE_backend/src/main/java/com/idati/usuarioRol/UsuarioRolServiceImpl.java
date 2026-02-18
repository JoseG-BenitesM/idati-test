package com.idati.usuarioRol;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioRolServiceImpl implements UsuarioRolService {
    @PersistenceContext
    private final EntityManager entityManager;
    
    @Override
    @Transactional
    public UsuarioRolResponseDTO ejecutarAccion(UsuarioRolRequestDTO dto) {
        String accion = dto.getAccion().toUpperCase().trim();
        
        Object[] datosUsuarioRol = null;
        if("ELIMINAR".equals(accion)) {
            try {
                datosUsuarioRol = (Object[]) entityManager.createNativeQuery(
                                "SELECT u.id, u.username, r.nombre, ur.fecha_asignacion " +
                                        "FROM usuarios_roles ur " +
                                        "JOIN usuarios u ON ur.id_usuario = u.id " +
                                        "JOIN roles r ON ur.id_rol = r.id " +
                                        "WHERE ur.id_usuario = ?1 AND ur.id_rol = ?2")
                        .setParameter(1, dto.getIdUsuario())
                        .setParameter(2, dto.getIdRol())
                        .getSingleResult();
            } catch (NoResultException e) {
                return UsuarioRolResponseDTO.builder()
                        .idUsuario(dto.getIdUsuario())
                        .nombreUsuario(null)
                        .rol(null)
                        .accion(accion)
                        .mensaje("ERROR: La asignacion usuario-rol no existe")
                        .build();
            }
        }
        
        // FALTA TERMINAR
    }
}
