package com.example.Empleados.controllers;

import com.example.Empleados.models.Empleado;
import com.example.Empleados.models.dtos.EmpleadoDto;
import com.example.Empleados.services.EmpleadoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {
    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @GetMapping
    public ResponseEntity<Iterable<EmpleadoDto>> getAllEmpleados(){
        try {
            Iterable<Empleado> empleados = empleadoService.getAll();
            List<EmpleadoDto> empleadosDtos = StreamSupport.stream(empleados.spliterator(), false)
                    .map(e -> new EmpleadoDto(e.getLegajo(), e.getNombre(), e.getApellido(), e.getTelefono()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(empleadosDtos);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("Error message:", e.getMessage())
                    .build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoDto> getVehiculoById(@PathVariable("id") int id){
        try {
            Empleado eEncontrado = empleadoService.getById(id);
            EmpleadoDto eDto = new EmpleadoDto(eEncontrado.getLegajo(),eEncontrado.getNombre(),eEncontrado.getApellido(),eEncontrado.getTelefono());
            return ResponseEntity.ok(eDto);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("Error message:", e.getMessage())
                    .build();
        }
    }

    @PostMapping("/crearEmpleado")
    public ResponseEntity<EmpleadoDto> crearPrueba(@RequestBody EmpleadoDto empleadoDetails){
        try{
            Empleado nuevoEmpleado = new Empleado();
            nuevoEmpleado.setNombre(empleadoDetails.getNombre());
            nuevoEmpleado.setApellido(empleadoDetails.getApellido());
            nuevoEmpleado.setTelefono(empleadoDetails.getTelefono());

            Empleado empleadoCreado  = empleadoService.create(nuevoEmpleado);

            EmpleadoDto empleadoCreadoDto = new EmpleadoDto(
                    empleadoCreado.getLegajo(),
                    empleadoCreado.getNombre(),
                    empleadoCreado.getApellido(),
                    empleadoCreado.getTelefono()
            );


            return ResponseEntity.ok(empleadoCreadoDto);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header("Error message:", e.getMessage())
                    .build();
        }

    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<EmpleadoDto> finalizarPrueba(@PathVariable("id") int id,
                                                     @RequestBody EmpleadoDto empleadoDetails
    ){
        try{
            Empleado empleadoActualizado = empleadoService.update(id, empleadoDetails);
            EmpleadoDto empleadoDto = new EmpleadoDto(
                    empleadoActualizado.getLegajo(),
                    empleadoActualizado.getNombre(),
                    empleadoActualizado.getApellido(),
                    empleadoActualizado.getTelefono()
            );

            return ResponseEntity.ok(empleadoDto);

        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("Error message:", e.getMessage())
                    .build();
        }

    }

    @GetMapping("/telefonos")
    public ResponseEntity<List<Long>> obtenerTelefonos(){
        Iterable<Empleado> empleados = empleadoService.getAll();
        List<Long> telefonos = StreamSupport.stream(empleados.spliterator(), false)
                .map(Empleado::getTelefono).collect(Collectors.toList());
        return ResponseEntity.ok(telefonos);
    }



}
