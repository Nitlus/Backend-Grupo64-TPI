package com.example.Pruebas.services;

import com.example.Pruebas.models.entities.Posicion;
import com.example.Pruebas.models.geoLocalizacion.ConfiguracionGeoLocalizacion;
import com.example.Pruebas.repositories.PosicionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class PosicionService {
    private final PosicionRepository posicionRepository;
    private final RestTemplate restTemplate;
    @Value("${api.service.url}")
    private String configuracionURL;

    @Autowired
    public PosicionService(PosicionRepository posicionRepository, RestTemplate restTemplate) {
        this.posicionRepository = posicionRepository;
        this.restTemplate = restTemplate;
    }


    public ConfiguracionGeoLocalizacion obtenerGeoLocalizacion(){
        ConfiguracionGeoLocalizacion config = restTemplate.getForObject(configuracionURL, ConfiguracionGeoLocalizacion.class);
        System.out.println("Configuración obtenida: " + config);
        return config;
    }

    private boolean estaDentroDelRadioPermitido(ConfiguracionGeoLocalizacion.Coordenada centro,
        Posicion posicion, double radioKm){
        double distancia = calcularDistancia(centro.getLat(), centro.getLon(), posicion.getLatitud(), posicion.getLongitud());
        return distancia <= radioKm;
    }


    private boolean estaEnZonaRestringida(ConfiguracionGeoLocalizacion.ZonaRestringida zona, Posicion posicion) {
        double lat = posicion.getLatitud();
        double lon = posicion.getLongitud();
        double latNoroeste = Math.max(zona.getNoroeste().getLat(), zona.getSureste().getLat());
        double latSureste = Math.min(zona.getNoroeste().getLat(), zona.getSureste().getLat());
        double lonNoroeste = Math.min(zona.getNoroeste().getLon(), zona.getSureste().getLon());
        double lonSureste = Math.max(zona.getNoroeste().getLon(), zona.getSureste().getLon());

        return lat <= latNoroeste && lat >= latSureste && lon >= lonNoroeste && lon <= lonSureste;
    }


    private double calcularDistancia(double lat1, double lon1, double lat2, double lon2){
        //Fórmula de Haversine para calcular la distancia
        final int EARTH_RADIUS = 6371; // Radio de la Tierra en km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }

    public boolean esPosicionPeligrosa(Posicion pos){
        ConfiguracionGeoLocalizacion config = obtenerGeoLocalizacion();

        if(!estaDentroDelRadioPermitido(config.getCoordenadasAgencia(), pos, config.getRadioAdmitidoKm())){
            System.out.println("La posición está fuera del radio permitido.");
            return true;
        }

        for(ConfiguracionGeoLocalizacion.ZonaRestringida zona : config.getZonasRestringidas()){
            if(estaEnZonaRestringida(zona, pos)){
                System.out.println("La posición está en una zona restringida.");
                return true;
            }
        }
        System.out.println("La posición es segura.");
        return false;
    }

    @Transactional
    public Posicion create(Posicion posicion){
        return posicionRepository.save(posicion);

    }

    public Iterable<Posicion> getAll(){
        return posicionRepository.findAll();

    }

    public Optional<Posicion> obtenerUltimaPosicionVehiculo(int vehiculoId){
        return posicionRepository.findTopByVehiculoIdOrderByFechaHoraDesc(vehiculoId);
    }

    public List<Posicion> getPosicionesVehiculoId(int vehiculoId){
        return posicionRepository.findByVehiculoId(vehiculoId);
    }




    public double calcularDistanciaKmPosicionesVehiculo(List<Posicion> posiciones){
        double distanciaTotal = 0.0;

        List<Posicion> posicionesOrdenadas = posiciones.stream()
                .sorted(Comparator.comparing(Posicion::getFechaHora))
                .toList();

        if (posicionesOrdenadas.size() < 2) {
            return distanciaTotal;
        }

        for(int i = 0; i < posicionesOrdenadas.size() - 1; i++){
            Posicion actual = posicionesOrdenadas.get(i);
            Posicion siguiente = posicionesOrdenadas.get(i+1);

            distanciaTotal += calcularDistancia(actual.getLatitud(), actual.getLongitud(), siguiente.getLatitud(), siguiente.getLongitud());
        }

        return distanciaTotal;
    }









}
