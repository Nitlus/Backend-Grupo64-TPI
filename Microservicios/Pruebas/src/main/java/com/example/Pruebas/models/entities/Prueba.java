package com.example.Pruebas.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "Pruebas")
public class Prueba {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    @ManyToOne
    @JoinColumn(name = "ID_VEHICULO", referencedColumnName = "ID")
    private Vehiculo vehiculo;
    @ManyToOne
    @JoinColumn(name = "ID_INTERESADO", referencedColumnName = "ID")
    private Interesado interesado;
    @Column(name = "ID_EMPLEADO")
    private int empleadoId;
    @Column(name = "FECHA_HORA_INICIO")
    private LocalDateTime fechaHoraInicio;
    @Column(name = "FECHA_HORA_FIN")
    private LocalDateTime fechaHoraFin;
    @Column(name = "COMENTARIOS")
    private String comentarios;
    @Column(name = "FINALIZADA")
    private boolean finalizada;



}
