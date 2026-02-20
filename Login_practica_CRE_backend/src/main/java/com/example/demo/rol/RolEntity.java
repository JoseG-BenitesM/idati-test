package com.example.demo.rol;

// CORRECCIÓN: Import de UsuarioRolEntity corregido
import com.example.demo.usuarioRol.UsuarioRolEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class RolEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(unique = true, nullable = false, length = 32)
    private String rolNombre;
    
    @Column(length = 128)
    private String descripcion;
    
    @OneToMany(mappedBy = "rol", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<UsuarioRolEntity> usuarioRoles;
}