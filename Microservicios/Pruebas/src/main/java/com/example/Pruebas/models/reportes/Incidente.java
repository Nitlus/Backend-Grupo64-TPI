package com.example.Pruebas.models.reportes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Incidente {
    private int pruebaId;
    private int vehiculoId;
    private String patenteVehiculo;
    private String marcaVehiculo;
    private String modeloVehiculo;
    private LocalDateTime posicionFechaHora;


}
