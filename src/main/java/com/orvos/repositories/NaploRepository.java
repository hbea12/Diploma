package com.orvos.repositories;


import com.orvos.models.Naplo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NaploRepository extends JpaRepository<Naplo, Integer>{
    List<Naplo> findByTaj(String taj);
    List<Naplo> findByOrvosAzonosito(String orvosAzonosito);
    List<Naplo> findByLatogatasDatumBetween(String from, String to);


}
