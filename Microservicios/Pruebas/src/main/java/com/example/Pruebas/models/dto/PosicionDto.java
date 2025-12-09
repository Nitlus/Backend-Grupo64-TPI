package com.example.Pruebas.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PosicionDto {
    private int id;
    private int vehiculoId;
    private LocalDateTime fechaHora;
    private double latitud;
    private double longitud;
}
