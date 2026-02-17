package com.idati.usuario;

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
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "usuarios")

public class UsuarioEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false, length = 32)
    private String usuarioNombre;
    
    @Column(unique = true, nullable = false, length = 128)
    private String correoElectronico;
    
    @Column(nullable = false, length = 128)
    private String contrasena;

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private boolean estadoUsuario;
    
    @Column(columnDefinition = "TINYINT DEFAULT 0")
    private int intentosFallidos;
    
    @Column(length = 64)
    private String tokenRecuperacion;

    private LocalDateTime fechaUltimoIntento;
    private LocalDateTime fechaTknExpiracion;
    
    @Column(name = "fecha_alta", insertable = false, updatable = false)
    private LocalDateTime fechaAlta;
    private LocalDateTime fechaBaja;
}