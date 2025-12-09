package com.example.Pruebas.repositories;

import com.example.Pruebas.models.entities.Prueba;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PruebaRepository extends CrudRepository<Prueba, Integer> {
    @Query("SELECT p FROM Prueba p JOIN p.vehiculo v WHERE p.vehiculo.id = :vehiculoId AND " +
            "(p.finalizada = false)")
    List<Prueba> findConflictingPruebas(@Param("vehiculoId") int vehiculoId);

    @Query("SELECT p FROM Prueba p WHERE p.finalizada = false")
    List<Prueba> findPruebasEnCurso();

}
