package com.example.Pruebas.controllers;

import com.example.Pruebas.models.entities.Interesado;
import com.example.Pruebas.models.entities.Prueba;
import com.example.Pruebas.models.entities.Vehiculo;
import com.example.Pruebas.models.dto.EmpleadoDto;
import com.example.Pruebas.models.dto.PruebaDto;
import com.example.Pruebas.services.InteresadoService;
import com.example.Pruebas.services.PruebaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/pruebas")
public class PruebaController {
    private final PruebaService pruebaService;


    @Autowired
    public PruebaController(PruebaService pruebaService) {
        this.pruebaService = pruebaService;
    }



    @PostMapping("/nuevaPrueba")
    public ResponseEntity<PruebaDto> crearPrueba(@RequestBody PruebaDto pruebaDto) {
        try {

            Vehiculo vehiculo = pruebaService.obtenerVehiculoPorId(pruebaDto.getVehiculoId());


            Interesado interesado = pruebaService.obtenerInteresadoPorId(pruebaDto.getInteresadoId());
            if (interesado.isRestringido()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Error message", "El interesado con ID " + pruebaDto.getInteresadoId() + " está restringido.")
                        .build();
            }

            EmpleadoDto empleado = pruebaService.obtenerEmpleado(pruebaDto.getEmpleadoId());

            // Verificar si la licencia del interesado está vencida
            LocalDateTime fechaVencimiento = interesado.getFechaVencLicencia();
            if (fechaVencimiento.isBefore(LocalDateTime.now())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .header("Error message", "La licencia del interesado está vencida. No se puede crear la prueba.")
                        .build();
            }

            // Crear nueva prueba
            Prueba nuevaPrueba = new Prueba();
            nuevaPrueba.setVehiculo(vehiculo);
            nuevaPrueba.setInteresado(interesado);
            nuevaPrueba.setEmpleadoId(empleado.getLegajo());
            nuevaPrueba.setFechaHoraInicio(LocalDateTime.now().minusHours(1));
            nuevaPrueba.setFechaHoraFin(LocalDateTime.now().minusHours(1));
            nuevaPrueba.setComentarios(null);
            nuevaPrueba.setFinalizada(false);

            // Guardar la prueba
            Prueba pruebaCreada = pruebaService.create(nuevaPrueba);

            // Crear el DTO para la respuesta
            PruebaDto pruebaCreadaDto = new PruebaDto(
                    pruebaCreada.getId(),
                    pruebaCreada.getVehiculo().getId(),
                    pruebaCreada.getInteresado().getId(),
                    pruebaCreada.getEmpleadoId(),
                    pruebaCreada.getFechaHoraInicio(),
                    pruebaCreada.getFechaHoraFin(),
                    pruebaCreada.getComentarios()
            );

            return ResponseEntity.ok(pruebaCreadaDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header("Error message:", e.getMessage())
                    .build();
        }
    }


    @GetMapping
    public ResponseEntity<Iterable<PruebaDto>> getAllPruebas(){
        try {
            Iterable<Prueba> pruebas = pruebaService.getAll();
            List<PruebaDto> pruebasDtos = StreamSupport.stream(pruebas.spliterator(), false)
                    .map(p -> new PruebaDto(p.getId(), p.getVehiculo().getId(), p.getInteresado().getId(), p.getEmpleadoId(), p.getFechaHoraInicio(), p.getFechaHoraFin(), p.getComentarios()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(pruebasDtos);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("Error message:", e.getMessage())
                    .build();
        }
    }

    @GetMapping("/enCurso")
    public ResponseEntity<List<PruebaDto>> obtenerPruebasEnCurso(){
        try {
            List<Prueba> pruebas = pruebaService.getPruebasEnCurso();
            List<PruebaDto> pruebasDtos = pruebas.stream()
                    .map(p -> new PruebaDto(p.getId(), p.getVehiculo().getId(), p.getInteresado().getId(), p.getEmpleadoId(), p.getFechaHoraInicio(), p.getFechaHoraFin(), p.getComentarios()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(pruebasDtos);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("Error message:", e.getMessage())
                    .build();
        }
    }

    @PutMapping("/finalizar/{id}")
    public ResponseEntity<PruebaDto> finalizarPrueba(@PathVariable("id") int id,
                                                     @RequestBody PruebaDto pruebaDetails
    ){
        try{
            Prueba pruebaFinalizada = pruebaService.finalizar(id, pruebaDetails.getComentarios());
            PruebaDto pruebaDto = new PruebaDto(pruebaFinalizada.getId(),
                    pruebaFinalizada.getVehiculo().getId(),
                    pruebaFinalizada.getInteresado().getId(),
                    pruebaFinalizada.getEmpleadoId(),
                    pruebaFinalizada.getFechaHoraInicio(),
                    pruebaFinalizada.getFechaHoraFin(),
                    pruebaFinalizada.getComentarios());

            return ResponseEntity.ok(pruebaDto);

        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("Error message:", e.getMessage())
                    .build();
        }

    }













}
