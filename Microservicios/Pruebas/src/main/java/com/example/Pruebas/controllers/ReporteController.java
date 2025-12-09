package com.example.Pruebas.controllers;

import com.example.Pruebas.models.entities.Posicion;
import com.example.Pruebas.models.entities.Prueba;
import com.example.Pruebas.models.reportes.DetallePruebaVehiculo;
import com.example.Pruebas.models.reportes.KilometrajeVehiculo;
import com.example.Pruebas.models.entities.Vehiculo;
import com.example.Pruebas.models.reportes.DetalleIncidenteEmpleado;
import com.example.Pruebas.models.dto.EmpleadoDto;
import com.example.Pruebas.models.reportes.Incidente;
import com.example.Pruebas.services.PosicionService;
import com.example.Pruebas.services.PruebaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {
    private final PruebaService pruebaService;
    private final PosicionService posicionService;
    private final RestTemplate restTemplate;
    @Value("${empleados.service.url}")
    private String empleadoURL;

    public ReporteController(PruebaService pruebaService, PosicionService posicionService, RestTemplate restTemplate) {
        this.pruebaService = pruebaService;
        this.posicionService = posicionService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/incidentes")
    public ResponseEntity<List<Incidente>> generarReporteIncidentes(){
        try{
            List<Prueba> pruebasFinalizadas = StreamSupport.stream(pruebaService.getAll().spliterator(), false)
                    .filter(Prueba::isFinalizada)
                    .toList();
            List<Incidente> incidentes = new ArrayList<>();
            for(Prueba prueba : pruebasFinalizadas){
                Vehiculo vehiculo = prueba.getVehiculo();
                List<Posicion> posiciones = posicionService.getPosicionesVehiculoId(prueba.getVehiculo().getId());
                for (Posicion pos : posiciones){
                    if(posicionService.esPosicionPeligrosa(pos)){
                        Incidente incidente = new Incidente();
                        incidente.setPruebaId(prueba.getId());
                        incidente.setVehiculoId(vehiculo.getId());
                        incidente.setPatenteVehiculo(vehiculo.getPatente());
                        incidente.setMarcaVehiculo(vehiculo.getModelo().getMarca().getName());
                        incidente.setModeloVehiculo(vehiculo.getModelo().getDescripcion());
                        incidente.setPosicionFechaHora(pos.getFechaHora());

                        incidentes.add(incidente);
                        break;
                    }
                }
            }
            return incidentes.isEmpty()
                    ? ResponseEntity.noContent().build()
                    : ResponseEntity.ok(incidentes);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("Error message:", e.getMessage())
                    .build();
        }

    }

    @GetMapping("/incidentes/empleado/{empleadoId}")
    public ResponseEntity<DetalleIncidenteEmpleado> generarReporteIncidentesPorEmpleado(@PathVariable int empleadoId){
        try{
            List<Prueba> pruebasFinalizadas = StreamSupport.stream(pruebaService.getAll().spliterator(), false)
                    .filter(Prueba::isFinalizada)
                    .toList();
            List<Incidente> incidentes = new ArrayList<>();
            List<Prueba> pruebasEmpleado = pruebasFinalizadas.stream()
                    .filter(prueba -> prueba.getEmpleadoId() == empleadoId)
                    .toList();
            for (Prueba prueba : pruebasEmpleado) {
                Vehiculo vehiculo = prueba.getVehiculo();
                List<Posicion> posiciones = posicionService.getPosicionesVehiculoId(prueba.getVehiculo().getId());
                for (Posicion pos : posiciones) {
                    if (posicionService.esPosicionPeligrosa(pos)) {
                        Incidente incidente = new Incidente();
                        incidente.setPruebaId(prueba.getId());
                        incidente.setVehiculoId(vehiculo.getId());
                        incidente.setPatenteVehiculo(vehiculo.getPatente());
                        incidente.setMarcaVehiculo(vehiculo.getModelo().getMarca().getName());
                        incidente.setModeloVehiculo(vehiculo.getModelo().getDescripcion());
                        incidente.setPosicionFechaHora(pos.getFechaHora());
                        incidentes.add(incidente);
                        break;
                    }
                }
            }
            ResponseEntity<EmpleadoDto> empleadoResponse = restTemplate.getForEntity(
                    empleadoURL + "/" + empleadoId, EmpleadoDto.class);
            if (empleadoResponse.getStatusCode() == HttpStatus.OK){
                EmpleadoDto empleado = empleadoResponse.getBody();

                DetalleIncidenteEmpleado detalle = new DetalleIncidenteEmpleado();
                detalle.setIdEmpleado(empleadoId);
                detalle.setNombreEmpleado(empleado.getNombre());
                detalle.setApellidoEmpleado(empleado.getApellido());
                detalle.setIncidentes(incidentes);
                return incidentes.isEmpty()
                        ? ResponseEntity.noContent().build()
                        : ResponseEntity.ok(detalle);
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Error", "Empleado no encontrado")
                        .build();
            }

        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("Error message", e.getMessage())
                    .build();
        }
    }

    @GetMapping("/km-recorridos-vehiculo/{vehiculoId}")
    public ResponseEntity<List<KilometrajeVehiculo>> generarReporteKmRecorridosVehiculo(@PathVariable int vehiculoId){
        try {
            List<Prueba> pruebasFinalizadas = StreamSupport.stream(pruebaService.getAll().spliterator(), false)
                    .filter(Prueba::isFinalizada)
                    .toList();

            List<Prueba> pruebasVehiculo = pruebasFinalizadas.stream()
                    .filter(prueba -> prueba.getVehiculo().getId() == vehiculoId)
                    .toList();

            List<KilometrajeVehiculo> kilometrajes = new ArrayList<>();

            if (pruebasVehiculo.stream().noneMatch(prueba -> prueba.getVehiculo().getId() == vehiculoId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Error", "Vehiculo no encontrado")
                        .build();
            }


            for (Prueba prueba : pruebasVehiculo){
                List<Posicion> posiciones = posicionService.getPosicionesVehiculoId(prueba.getVehiculo().getId());
                double kmRecorridos = posicionService.calcularDistanciaKmPosicionesVehiculo(posiciones);
                KilometrajeVehiculo kilometrajeVehiculo = new KilometrajeVehiculo(
                        vehiculoId,
                        prueba.getVehiculo().getPatente(),
                        kmRecorridos,
                        prueba.getFechaHoraInicio(),
                        prueba.getFechaHoraFin());
                kilometrajes.add(kilometrajeVehiculo);
            }
            return kilometrajes.isEmpty()
                    ? ResponseEntity.noContent().build()
                    : ResponseEntity.ok(kilometrajes);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("Error message", e.getMessage())
                    .build();
        }

    }


    @GetMapping("/pruebasVehiculo/{vehiculoId}")
    public ResponseEntity<List<DetallePruebaVehiculo>>  generarReporteDetallePruebasVehiculo(@PathVariable int vehiculoId){
        try{
            List<Prueba> pruebasFinalizadas = StreamSupport.stream(pruebaService.getAll().spliterator(), false)
                    .filter(Prueba::isFinalizada)
                    .toList();

            List<Prueba> pruebasVehiculo = pruebasFinalizadas.stream()
                    .filter(prueba -> prueba.getVehiculo().getId() == vehiculoId)
                    .toList();

            List<DetallePruebaVehiculo> detallePruebaVehiculos = new ArrayList<>();
            for (Prueba prueba : pruebasVehiculo){
                DetallePruebaVehiculo detallePruebaVehiculo = new DetallePruebaVehiculo(vehiculoId,
                        prueba.getVehiculo().getPatente(),
                        prueba.getVehiculo().getModelo().getMarca().getName(),
                        prueba.getVehiculo().getModelo().getDescripcion(),
                        prueba.getInteresado().getNombre(),
                        prueba.getInteresado().getApellido(),
                        prueba.getFechaHoraInicio(),
                        prueba.getFechaHoraFin(),
                        prueba.getComentarios()
                        );

                detallePruebaVehiculos.add(detallePruebaVehiculo);

            }
            return detallePruebaVehiculos.isEmpty()
                    ? ResponseEntity.noContent().build()
                    : ResponseEntity.ok(detallePruebaVehiculos);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("Error message", e.getMessage())
                    .build();
        }

    }



}
