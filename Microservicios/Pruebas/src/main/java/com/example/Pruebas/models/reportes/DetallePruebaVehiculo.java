package com.example.Pruebas.models.reportes;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetallePruebaVehiculo {
    private int vehiculoId;
    private String patente;
    private String marca;
    private String modelo;
    private String interesadoNombre;
    private String interesadoApellido;
    private LocalDateTime fechaHoraInicioPrueba;
    private LocalDateTime fechaHoraFinPrueba;
    private String comentarios;

}
