package com.example.Pruebas.repositories;

import com.example.Pruebas.models.entities.Posicion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PosicionRepository extends CrudRepository<Posicion, Integer> {

    Optional<Posicion> findTopByVehiculoIdOrderByFechaHoraDesc(int vehiculoId);

    List<Posicion> findByVehiculoId(int vehiculoId);
}
