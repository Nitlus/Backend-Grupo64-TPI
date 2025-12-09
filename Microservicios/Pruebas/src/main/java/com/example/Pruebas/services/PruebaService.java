package com.example.Pruebas.services;

import com.example.Pruebas.models.entities.Interesado;
import com.example.Pruebas.models.entities.Prueba;


import com.example.Pruebas.models.entities.Vehiculo;
import com.example.Pruebas.models.dto.EmpleadoDto;
import com.example.Pruebas.repositories.InteresadoRepository;
import com.example.Pruebas.repositories.PruebaRepository;
import com.example.Pruebas.repositories.VehiculoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PruebaService {
    private final PruebaRepository pruebaRepository;
    private final VehiculoRepository vehiculoRepository;
    private final InteresadoRepository interesadoRepository;
    private final RestTemplate restTemplate;
    @Value("${empleados.service.url}")
    private String empleadosURL;

    @Autowired
    public PruebaService(PruebaRepository pruebaRepository,
                         VehiculoRepository vehiculoRepository,
                         InteresadoRepository interesadoRepository,
                         RestTemplate restTemplate) {
        this.pruebaRepository = pruebaRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.interesadoRepository = interesadoRepository;
        this.restTemplate = restTemplate;
    }

    public EmpleadoDto obtenerEmpleado(int id){
        String url =empleadosURL + "/" + id;
            return restTemplate.getForObject(url, EmpleadoDto.class);
    }

    @Transactional
    public Prueba create(Prueba prueba) throws Exception {
        List<Prueba> conflictos = pruebaRepository.findConflictingPruebas(prueba.getVehiculo().getId());
        if(!conflictos.isEmpty()){
            throw new Exception("El vehiculo ya esta siendo probado");
        }
        System.out.println("Guardando prueba: " + prueba);
        return pruebaRepository.save(prueba);
    }

    public Vehiculo obtenerVehiculoPorId(int id) throws Exception{
        return vehiculoRepository.findById(id).orElseThrow(()->
                new Exception("No se encontro vehiculo " + id));
    }

    public Interesado obtenerInteresadoPorId(int id) throws Exception{
        return interesadoRepository.findById(id).orElseThrow(()->
                new Exception("No se encontro interesado "));
    }

    public Prueba getById(int id) throws Exception{
        return pruebaRepository.findById(id).orElseThrow(()->
                new Exception("No se encontro prueba"));
    }

    public Iterable<Prueba> getAll(){
        return pruebaRepository.findAll();
    }

    public List<Prueba> getPruebasEnCurso(){
        return pruebaRepository.findPruebasEnCurso();
    }

    @Transactional
    public Prueba finalizar(int id, String comentarios) throws Exception {
        Prueba prueba = getById(id);
        if (prueba.isFinalizada()) { // Verifica si ya está finalizada
            throw new Exception("La prueba ya está finalizada");
        }
        prueba.setFechaHoraFin(LocalDateTime.now());
        prueba.setComentarios(comentarios);
        prueba.setFinalizada(true);


        return pruebaRepository.save(prueba);

    }





}
