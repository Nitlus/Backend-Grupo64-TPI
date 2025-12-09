package com.example.Pruebas.controllers;

import com.example.Pruebas.models.entities.Interesado;
import com.example.Pruebas.models.dto.InteresadoDto;
import com.example.Pruebas.services.InteresadoService;
import com.example.Pruebas.services.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/interesados")
public class InteresadoController {
    private final InteresadoService interesadoService;
    private final NotificacionService notificacionService;

    @Autowired
    public InteresadoController(InteresadoService interesadoService, NotificacionService notificacionService) {
        this.interesadoService = interesadoService;
        this.notificacionService = notificacionService;
    }

    @PostMapping("/cargar")
    public ResponseEntity<String> cargarInteresadosCSV(@RequestParam("file") MultipartFile csv){
        if (csv.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El archivo CSV está vacío");
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(csv.getInputStream(), StandardCharsets.UTF_8))) {
            interesadoService.cargarDatosCSV(br);
            return ResponseEntity.ok("Datos cargados exitosamente desde el archivo CSV");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al cargar los datos desde el archivo CSV");
        }


    }


    @GetMapping("/restringidos")
    public ResponseEntity<List<InteresadoDto>> obtenerInteresadosRestringidos(){
        try {
            List<Interesado> interesadosRestringidos = interesadoService.getInteresdosRestringidos();
            List<InteresadoDto> interesadosDtos = interesadosRestringidos.stream()
                    .map(i -> new InteresadoDto(i.getId(),i.getTipoDocumento(),i.getDocumento(),i.getNombre(),i.getApellido(),i.isRestringido(),i.getNroLicencia(),i.getFechaVencLicencia(),i.getTelefono()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(interesadosDtos);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("Error message:", e.getMessage())
                    .build();
        }
    }

    @PostMapping("/enviarPromocion")
    public ResponseEntity<String> enviarPromocionInteresados(@RequestParam String descripcion){
        List<Long> telefonosInteresados = interesadoService.obtenerTelefonosInteresados();
        if (telefonosInteresados.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No hay interesados disponibles para recibir la promoción.");
        }

        Random random = new Random();

        int cantidadSeleccionada = random.nextInt(telefonosInteresados.size()) + 1;
        List<Long> telefonosSeleccionados = telefonosInteresados.stream()
                .limit(cantidadSeleccionada)
                .collect(Collectors.toList());

        try{
            notificacionService.enviarNotificacionPromocion(descripcion, telefonosSeleccionados);
            return ResponseEntity.ok("Notificación de promoción enviada a " + telefonosSeleccionados.size() + " interesados.");
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al enviar notificación: " + e.getMessage());
        }

    }





}
