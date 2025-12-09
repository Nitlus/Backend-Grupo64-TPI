package com.example.Notificaciones.service;

import com.example.Notificaciones.models.Notificacion;
import com.example.Notificaciones.repositories.NotificacionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class NotificacionService {
    private final NotificacionRepository notificacionRepository;
    private final RestTemplate restTemplate;
    @Value("${empleados.service.url}")
    private String empleadosURL;


    @Autowired
    public NotificacionService(NotificacionRepository notificacionRepository, RestTemplate restTemplate) {
        this.notificacionRepository = notificacionRepository;
        this.restTemplate = restTemplate;
    }


    @Transactional
    public Notificacion create(Notificacion notificacion){
        return notificacionRepository.save(notificacion);
    }

    public Iterable<Notificacion> getAll(){
        return notificacionRepository.findAll();
    }





}
