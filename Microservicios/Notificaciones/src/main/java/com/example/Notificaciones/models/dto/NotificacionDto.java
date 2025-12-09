package com.example.Notificaciones.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificacionDto {
    private int id;
    private String descripcion;
    private List<Long> telefonos;
    private String tipo;

}
