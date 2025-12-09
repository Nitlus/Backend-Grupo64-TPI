package com.example.Pruebas.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "Interesados")
public class Interesado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    @Column(name = "TIPO_DOCUMENTO")
    private String tipoDocumento;
    @Column(name = "DOCUMENTO")
    private String documento;
    @Column(name = "NOMBRE")
    private String nombre;
    @Column(name = "APELLIDO")
    private String apellido;
    @Column(name = "RESTRINGIDO")
    private boolean restringido;
    @Column(name = "NRO_LICENCIA")
    private int nroLicencia;
    @Column(name = "FECHA_VENCIMIENTO_LICENCIA")
    private LocalDateTime fechaVencLicencia;
    @Column(name = "TELEFONO")
    private Long telefono;
    @OneToMany(mappedBy = "interesado")
    @ToString.Exclude
    private Set<Prueba> pruebas;

}
