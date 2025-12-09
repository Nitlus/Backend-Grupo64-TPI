package com.example.Pruebas.models.reportes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KilometrajeVehiculo {
    private int vehiculoId;
    private String patente;
    private double kmRecorrido;
    private LocalDateTime fechaHoraInicioPrueba;
    private LocalDateTime fechaHoraFinPrueba;
}
