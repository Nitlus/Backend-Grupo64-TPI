package com.example.Empleados.services;

import com.example.Empleados.models.Empleado;
import com.example.Empleados.models.dtos.EmpleadoDto;
import com.example.Empleados.repositories.EmpleadoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpleadoService {
    private final EmpleadoRepository empleadoRepository;

    @Autowired
    public EmpleadoService(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    public Iterable<Empleado> getAll(){
        return empleadoRepository.findAll();
    }

    public Empleado getById(int id) throws Exception{
        return empleadoRepository.findById(id).orElseThrow(()->
                new Exception("No se encontro empleado"));
    }

    @Transactional
    public Empleado create(Empleado empleado) throws Exception {
        System.out.println("Guardando empleado: " + empleado);
        return empleadoRepository.save(empleado);
    }

    @Transactional
    public Empleado update(int id, EmpleadoDto empleadoDetails) throws Exception {
        Empleado empleado = getById(id);

        if (empleadoDetails.getNombre() != null) {
            empleado.setNombre(empleadoDetails.getNombre());
        }
        if (empleadoDetails.getApellido() != null) {
            empleado.setApellido(empleadoDetails.getApellido());
        }
        if (empleadoDetails.getTelefono() != null) {
            empleado.setTelefono(empleadoDetails.getTelefono());
        }

        return empleadoRepository.save(empleado);

    }



}
