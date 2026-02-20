package com.example.demo.usuario;

import com.example.demo.usuarioRol.UsuarioRolEntity;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
public class UsuarioEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "usuario_nombre", unique = true, nullable = false, length = 32)
    private String usuarioNombre;
    
    @Column(name = "correo_electronico", unique = true, nullable = false, length = 128)
    private String correoElectronico;
    
    @Column(name = "contrasena", nullable = false, length = 128)
    private String contrasena;

    // Byte mapea perfectamente a TINYINT en MySQL
    @Column(name = "estado_usuario", nullable = false, columnDefinition = "TINYINT")
    private Byte estadoUsuario;
    
    @Column(name = "intentos_fallidos", columnDefinition = "TINYINT")
    private Byte intentosFallidos;
    
    @Column(name = "token_recuperacion", length = 64)
    private String tokenRecuperacion;

    @Column(name = "fecha_ultimo_intento")
    private LocalDateTime fechaUltimoIntento;

    @Column(name = "fecha_tkn_expiracion")
    private LocalDateTime fechaTknExpiracion;
    
    @Column(name = "fecha_alta", nullable = false, insertable = false, updatable = false)
    private LocalDateTime fechaAlta;

    @Column(name = "fecha_baja")
    private LocalDateTime fechaBaja;
    
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<UsuarioRolEntity> usuarioRoles;

    // Helper para que tu lógica de negocio siga funcionando como si fuera booleano
    public boolean isActivo() {
        return this.estadoUsuario != null && this.estadoUsuario == 1;
    }
}