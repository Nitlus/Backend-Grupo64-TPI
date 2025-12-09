package com.example.Pruebas.models.geoLocalizacion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracionGeoLocalizacion {
    private Coordenada coordenadasAgencia;
    private double radioAdmitidoKm;
    private List<ZonaRestringida> zonasRestringidas;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Coordenada{
        private double lat;
        private double lon;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ZonaRestringida{
        private Coordenada noroeste;
        private Coordenada sureste;
    }






}
