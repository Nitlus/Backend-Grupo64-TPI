package com.example.Pruebas.models.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpleadoDto {
    private int legajo;
    private String nombre;
    private String apellido;
    private long telefono;
}
