package com.example.demo.usuarioRol;

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
    private EntityManager entityManager;

    @Override
    @Transactional
    public UsuarioRolResponseDTO ejecutarAccion(UsuarioRolRequestDTO dto) {
        String accion = dto.getAccion().toUpperCase().trim();
        try {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_usuario_rol_accion");
            query.registerStoredProcedureParameter("p_accion", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_id_usuario", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_id_rol", Integer.class, ParameterMode.IN);
            
            query.setParameter("p_accion", accion);
            query.setParameter("p_id_usuario", dto.getIdUsuario());
            query.setParameter("p_id_rol", dto.getIdRol());
            
            query.execute();

            return UsuarioRolResponseDTO.builder()
                    .idUsuario(dto.getIdUsuario())
                    .accion(accion)
                    .mensaje("Operación " + accion + " realizada con éxito")
                    .build();
        } catch (Exception e) {
            return UsuarioRolResponseDTO.builder()
                    .mensaje("ERROR: " + e.getMessage())
                    .build();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioRolResponseDTO> listar() {
        // CORREGIDO: r.rol_nombre en lugar de r.nombre_rol
        String sql = "SELECT u.id, u.usuario_nombre, r.rol_nombre FROM usuarios_roles ur " +
                     "JOIN usuarios u ON ur.id_usuario = u.id " +
                     "JOIN roles r ON ur.id_rol = r.id";
        
        List<Object[]> resultados = entityManager.createNativeQuery(sql).getResultList();
        
        return resultados.stream().map(row -> UsuarioRolResponseDTO.builder()
                .idUsuario((Integer) row[0])
                .nombreUsuario((String) row[1])
                .rol((String) row[2])
                .build()).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioRolResponseDTO> listarPorUsuario(Integer idUsuario) {
        // CORREGIDO: r.rol_nombre en lugar de r.nombre_rol
        String sql = "SELECT u.id, u.usuario_nombre, r.rol_nombre FROM usuarios_roles ur " +
                     "JOIN usuarios u ON ur.id_usuario = u.id " +
                     "JOIN roles r ON ur.id_rol = r.id WHERE u.id = ?1";
        
        List<Object[]> resultados = entityManager.createNativeQuery(sql)
                .setParameter(1, idUsuario)
                .getResultList();
        
        return resultados.stream().map(row -> UsuarioRolResponseDTO.builder()
                .idUsuario((Integer) row[0])
                .nombreUsuario((String) row[1])
                .rol((String) row[2])
                .build()).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UsuarioRolResponseDTO eliminar(Integer idUsuario, Integer idRol) {
        try {
            int deletedCount = entityManager.createNativeQuery(
                    "DELETE FROM usuarios_roles WHERE id_usuario = ?1 AND id_rol = ?2")
                    .setParameter(1, idUsuario)
                    .setParameter(2, idRol)
                    .executeUpdate();

            if (deletedCount > 0) {
                return UsuarioRolResponseDTO.builder()
                        .idUsuario(idUsuario)
                        .accion("ELIMINAR")
                        .mensaje("Asignación eliminada correctamente")
                        .build();
            } else {
                return UsuarioRolResponseDTO.builder()
                        .mensaje("ERROR: No se encontró la asignación para eliminar")
                        .build();
            }
        } catch (Exception e) {
            return UsuarioRolResponseDTO.builder()
                    .mensaje("ERROR: " + e.getMessage())
                    .build();
        }
    }
}