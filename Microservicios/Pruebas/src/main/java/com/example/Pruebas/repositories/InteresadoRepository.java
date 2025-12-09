package com.example.Pruebas.repositories;

import com.example.Pruebas.models.entities.Interesado;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface InteresadoRepository extends CrudRepository<Interesado, Integer> {
    @Query("SELECT i FROM Interesado i WHERE i.restringido = true")
    List<Interesado> findInteresadosRestringidos();

}
