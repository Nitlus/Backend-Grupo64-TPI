package com.example.Pruebas.models.reportes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalleIncidenteEmpleado {
    private int idEmpleado;
    private String nombreEmpleado;
    private String apellidoEmpleado;
    private List<Incidente> incidentes;
}
