package com.example.demo.solicitud;

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
@Table(name = "solicitudes_recuperacion")
public class SolicitudRecuperacionEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonIgnore
    private UsuarioEntity usuario;
    
    @Column(name = "codigo", nullable = false, length = 6)
    private String codigo;
    
    @Column(name = "fecha_solicitud", insertable = false, updatable = false)
    private LocalDateTime fechaSolicitud;
    
    @Column(name = "fecha_uso")
    private LocalDateTime fechaUso;
    
    @Column(name = "estado", columnDefinition = "TINYINT")
    private Byte estado;
    // 0 = pendiente
    // 1 = aprobada por admin
    // 2 = usada por usuario
    // 3 = expirada
}
