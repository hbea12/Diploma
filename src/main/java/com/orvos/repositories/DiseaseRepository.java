package com.orvos.repositories;

import com.orvos.models.Disease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiseaseRepository extends JpaRepository<Disease, Integer>{
    Disease findByBetegsegId(String betegsegId);
    //Disease findByBetegsegAzonosito(String betegsegAzonosito);
}
