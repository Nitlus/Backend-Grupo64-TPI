package com.example.Pruebas.services;


import com.example.Pruebas.models.entities.Interesado;
import com.example.Pruebas.repositories.InteresadoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;



@Service
public class InteresadoService {

    @Autowired
    private InteresadoRepository interesadoRepository;

    @Transactional
    public void cargarDatosCSV(BufferedReader br){
        try(Stream<String> lineas = br.lines()){
            lineas.skip(1).forEach(linea -> {
                String[] datos = linea.split("\\|");
                Interesado interesado = new Interesado();
                interesado.setTipoDocumento(datos[0]);
                interesado.setDocumento(datos[1]);
                interesado.setNombre(datos[2]);
                interesado.setApellido(datos[3]);
                interesado.setRestringido(Boolean.parseBoolean(datos[4]));
                interesado.setNroLicencia(Integer.parseInt(datos[5]));
                interesado.setFechaVencLicencia(LocalDateTime.parse(datos[6], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                interesado.setTelefono(Long.parseLong(datos[7]));
                // Guardar en la base de datos
                System.out.println("Guardando interesado: " + interesado);
                interesadoRepository.save(interesado);
            });
        }
        catch (Exception e) {
        e.printStackTrace();
        }

    }

    @Transactional
    public void update(Interesado interesado) {
        interesado.setRestringido(true);
        interesadoRepository.save(interesado);
    }

    public List<Interesado> getInteresdosRestringidos(){
        return interesadoRepository.findInteresadosRestringidos();
    }

    public Iterable<Interesado> getAll(){
        return interesadoRepository.findInteresadosRestringidos();
    }

    public List<Long> obtenerTelefonosInteresados() {
        return StreamSupport.stream(interesadoRepository.findAll().spliterator(), false)
                .filter(i -> !i.isRestringido())
                .map(Interesado::getTelefono)
                .collect(Collectors.toList());
    }






}
