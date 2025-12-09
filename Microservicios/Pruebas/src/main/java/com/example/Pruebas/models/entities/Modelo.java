package com.example.Pruebas.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "Modelos")
public class Modelo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    @ManyToOne
    @JoinColumn(name = "ID_MARCA", referencedColumnName = "ID")
    private Marca marca;
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @OneToMany(mappedBy = "modelo")
    @ToString.Exclude
    private Set<Vehiculo> vehiculos;
}
