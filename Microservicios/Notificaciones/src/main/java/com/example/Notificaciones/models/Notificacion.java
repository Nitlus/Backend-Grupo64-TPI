package com.example.Notificaciones.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "Notificaciones")
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @ElementCollection
    @CollectionTable(name = "Notificaciones_Telefonos", joinColumns = @JoinColumn(name = "NOTIFICACION_ID"))
    @Column(name = "TELEFONO")
    private List<Long> telefonos;
    @Column(name = "TIPO")
    private String tipo;


}
