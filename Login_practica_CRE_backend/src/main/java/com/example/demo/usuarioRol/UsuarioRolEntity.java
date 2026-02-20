package com.example.demo.usuarioRol;

import com.example.demo.rol.RolEntity;
import com.example.demo.usuario.UsuarioEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuarios_roles", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id_usuario", "id_rol"})})
public class UsuarioRolEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonIgnore
    private UsuarioEntity usuario;
    
    @ ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rol", nullable = false)
    @JsonIgnore
    private RolEntity rol;
    
    // CORRECCIÓN: Se añade nullable = false e insertable/updatable false 
    // porque normalmente es un TIMESTAMP con DEFAULT CURRENT_TIMESTAMP
    @Column(name = "fecha_asignacion", nullable = false, insertable = false, updatable = false)
    private LocalDateTime fechaAsignacion;
}