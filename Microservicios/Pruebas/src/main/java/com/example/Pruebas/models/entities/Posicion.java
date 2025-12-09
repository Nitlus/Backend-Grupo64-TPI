package com.example.Pruebas.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "Posiciones")
public class Posicion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    @ManyToOne
    @JoinColumn(name = "ID_VEHICULO", referencedColumnName = "ID")
    private Vehiculo vehiculo;
    @Column(name = "FECHA_HORA")
    private LocalDateTime fechaHora;
    @Column(name = "LATITUD")
    private double latitud;
    @Column(name = "LONGITUD")
    private double longitud;
}
