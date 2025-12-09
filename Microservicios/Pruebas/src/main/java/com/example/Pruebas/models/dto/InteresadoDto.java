package com.example.Pruebas.models.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InteresadoDto {
    private int id;
    private String tipoDocumento;
    private String documento;
    private String nombre;
    private String apellido;
    private boolean restringido;
    private int nroLicencia;
    private LocalDateTime fechaVencLicencia;
    private Long telefono;
}
