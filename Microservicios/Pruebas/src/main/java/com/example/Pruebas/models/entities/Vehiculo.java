package com.example.Pruebas.models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "Vehiculos")
public class Vehiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    @Column(name = "PATENTE")
    private String patente;
    @ManyToOne
    @JoinColumn(name = "ID_MODELO", referencedColumnName = "ID")
    private Modelo modelo;
    @Column(name = "ANIO")
    private int anio;
    @OneToMany(mappedBy = "vehiculo")
    @ToString.Exclude
    private Set<Posicion> posiciones;
    @OneToMany(mappedBy = "vehiculo")
    @ToString.Exclude
    private Set<Prueba> pruebas;



}
