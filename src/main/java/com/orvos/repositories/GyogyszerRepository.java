package com.orvos.repositories;

import com.orvos.models.Gyogyszer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GyogyszerRepository extends JpaRepository<Gyogyszer, Integer>{

    List<Gyogyszer> findByGyogyszerHatoanyag(String gyogyszerHatoanyag);
    Gyogyszer findByGyogyszerAzonosito(String gyogyszerAzonosito);

}
