package com.example.Notificaciones.controllers;

import com.example.Notificaciones.models.Notificacion;
import com.example.Notificaciones.models.dto.NotificacionDto;
import com.example.Notificaciones.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {
    private final NotificacionService notificacionService;

    @Autowired
    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @PostMapping("/crear")
    public ResponseEntity<NotificacionDto> crearNotificacion(@RequestBody NotificacionDto notificacionDto){
        try {

            Notificacion notificacionNueva = new Notificacion();
            notificacionNueva.setDescripcion(notificacionDto.getDescripcion());
            notificacionNueva.setTelefonos(notificacionDto.getTelefonos());
            notificacionNueva.setTipo(notificacionDto.getTipo());

            Notificacion creada = notificacionService.create(notificacionNueva);

            NotificacionDto creadaDto = new NotificacionDto(creada.getId(),
                    creada.getDescripcion(),
                    creada.getTelefonos(),
                    creada.getTipo());
            return ResponseEntity.ok(creadaDto);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header("Error message:", e.getMessage())
                    .build();
        }
    }

    @GetMapping
    public ResponseEntity<Iterable<NotificacionDto>> getAllNotificaciones(){
        try{
            Iterable<Notificacion> notificaciones = notificacionService.getAll();
            List<NotificacionDto> notificacionesDtos = StreamSupport.stream(notificaciones.spliterator(), false)
                    .map(n -> new NotificacionDto(n.getId(),n.getDescripcion(),n.getTelefonos(),n.getTipo()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(notificacionesDtos);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header("Error message:", e.getMessage())
                    .build();
        }

    }



}
