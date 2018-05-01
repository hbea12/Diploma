package com.orvos.repositories;

import com.orvos.models.Orvos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrvosRepository extends JpaRepository<Orvos, Integer>{
    Orvos findByOrvosAzonosito(String orvosId);
}
