package com.example.Pruebas.services;

import com.example.Pruebas.models.entities.Posicion;
import com.example.Pruebas.models.dto.NotificacionDto;
import com.example.Pruebas.repositories.InteresadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class NotificacionService {
    private final RestTemplate restTemplate;
    private final InteresadoRepository interesadoRepository;
    @Value("${notificacion.service.url}")
    private String notificacionServiceUrl;
    @Value("${empleados.service.url}")
    private String empleadosUrl;

    @Autowired
    public NotificacionService(RestTemplate restTemplate, InteresadoRepository interesadoRepository) {
        this.restTemplate = restTemplate;
        this.interesadoRepository = interesadoRepository;
    }


    private List<Long> obtenerNumerosTelefonosEmpleado(){
        String url = empleadosUrl + "/telefonos";
        ResponseEntity<List<Long>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Long>>() {}
        );
        return response.getBody();
    }



    public void enviarNotificacionAdvertencia(Posicion posicion) throws RuntimeException{
        NotificacionDto notificacionDto = new NotificacionDto();
        notificacionDto.setDescripcion("Advertencia de posición peligrosa para el vehículo con ID: " + posicion.getVehiculo().getId());
        List<Long> telefonos = obtenerNumerosTelefonosEmpleado();
        notificacionDto.setTelefonos(telefonos);
        notificacionDto.setTipo("Advertencia");

        ResponseEntity<NotificacionDto> response = restTemplate.exchange(
                notificacionServiceUrl + "/crear",
                HttpMethod.POST,
                new HttpEntity<>(notificacionDto),
                NotificacionDto.class
        );
        if (response.getStatusCode() == HttpStatus.OK){
            System.out.println("Notificación creada con éxito: " + response.getBody().getId());
        }
        else{
            throw new RuntimeException("Error al crear la notificación. Status: " + response.getStatusCode());
        }
    }

    public void enviarNotificacionPromocion(String descripcion, List<Long> telefonos) throws RuntimeException{
        NotificacionDto notificacionDto = new NotificacionDto();
        notificacionDto.setDescripcion(descripcion);
        notificacionDto.setTelefonos(telefonos);
        notificacionDto.setTipo("Promocion");

        ResponseEntity<NotificacionDto> response = restTemplate.exchange(
                notificacionServiceUrl + "/crear",
                HttpMethod.POST,
                new HttpEntity<>(notificacionDto),
                NotificacionDto.class
        );
        if (response.getStatusCode() == HttpStatus.OK){
            System.out.println("Notificación creada con éxito: " + response.getBody().getId());
        }
        else{
            throw new RuntimeException("Error al crear la notificación. Status: " + response.getStatusCode());
        }
    }






}
