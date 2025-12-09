package com.example.Pruebas.controllers;

import com.example.Pruebas.models.entities.Interesado;
import com.example.Pruebas.models.entities.Posicion;
import com.example.Pruebas.models.entities.Prueba;
import com.example.Pruebas.models.entities.Vehiculo;
import com.example.Pruebas.models.dto.PosicionDto;
import com.example.Pruebas.services.InteresadoService;
import com.example.Pruebas.services.NotificacionService;
import com.example.Pruebas.services.PosicionService;
import com.example.Pruebas.services.PruebaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/posiciones")
public class PosicionController {
    private final PosicionService posicionService;
    private final PruebaService pruebaService;
    private final NotificacionService notificacionService;
    private final InteresadoService interesadoService;


    @Autowired
    public PosicionController(PosicionService posicionService, PruebaService pruebaService, NotificacionService notificacionService, InteresadoService interesadoService) {
        this.posicionService = posicionService;
        this.pruebaService = pruebaService;
        this.notificacionService = notificacionService;
        this.interesadoService = interesadoService;
    }

    @GetMapping
    public ResponseEntity<Iterable<PosicionDto>> getAllPosicionesVehiculo(){
        try {
            Iterable<Posicion> posiciones = posicionService.getAll();
            List<PosicionDto> posicionesDto = StreamSupport.stream(posiciones.spliterator(), false)
                    .map(p -> new PosicionDto(p.getId(), p.getVehiculo().getId(), p.getFechaHora(),p.getLatitud(),p.getLongitud()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(posicionesDto);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("Error message:", e.getMessage())
                    .build();
        }

    }

    @PostMapping("/validar-posicion")
    public ResponseEntity<String> validarPosicion(@RequestParam int vehiculoId,
                                                  @RequestParam double latitud,
                                                  @RequestParam double longitud) {

        try {
            List<Prueba> pruebasEnCurso = new ArrayList<>(pruebaService.getPruebasEnCurso());
            Optional<Vehiculo> vehiculoEnPrueba = pruebasEnCurso.stream()
                    .map(Prueba::getVehiculo)
                    .filter(v -> v.getId() == vehiculoId)
                    .findFirst();

            if (vehiculoEnPrueba.isPresent()) {
                Vehiculo vehiculo = vehiculoEnPrueba.get();
                List<Posicion> posicionesPrevias = posicionService.getPosicionesVehiculoId(vehiculoId);

                Posicion posicion = new Posicion();
                posicion.setVehiculo(vehiculo);
                posicion.setFechaHora(LocalDateTime.now());

                // Si no hay posiciones previas, inicia en la agencia
                if (posicionesPrevias.isEmpty()) {
                    posicion.setLatitud(42.50886738457441);
                    posicion.setLongitud(1.5347139324337429);
                } else {
                    // Usa las coordenadas proporcionadas en el parámetro
                    posicion.setLatitud(latitud);
                    posicion.setLongitud(longitud);
                }

                posicionService.create(posicion);

                boolean esPeligrosa = posicionService.esPosicionPeligrosa(posicion);
                if (esPeligrosa){
                    notificacionService.enviarNotificacionAdvertencia(posicion);
                    Optional<Prueba> pruebaEnCurso = pruebasEnCurso.stream()
                            .filter(prueba -> prueba.getVehiculo().getId() == vehiculoId)
                            .findFirst();

                    if (pruebaEnCurso.isPresent()){
                        Prueba prueba = pruebaEnCurso.get();
                        Interesado interesado = prueba.getInteresado();
                        interesadoService.update(interesado);

                        Posicion posicionAgencia = new Posicion();
                        posicionAgencia.setVehiculo(vehiculo);
                        posicionAgencia.setLatitud(42.50886738457441);
                        posicionAgencia.setLongitud(1.5347139324337429);
                        posicionAgencia.setFechaHora(LocalDateTime.now());
                        posicionService.create(posicionAgencia);

                        pruebaService.finalizar(prueba.getId(), "El cliente ha sido restringido de la agencia");
                        return ResponseEntity.ok("Posición validada, zona restringida, regresar el vehiculo notificando empleados.");
                    }
                }
                return ResponseEntity.ok("Posición validada, zona segura.");
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El vehículo con ID " + vehiculoId + " no está en una prueba activa.");

            }
        }

        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("Error message", e.getMessage())
                    .build();
        }
    }



}
