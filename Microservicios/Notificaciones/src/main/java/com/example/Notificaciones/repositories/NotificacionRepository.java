package com.example.Notificaciones.repositories;

import com.example.Notificaciones.models.Notificacion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacionRepository extends CrudRepository<Notificacion, Integer> {
}

