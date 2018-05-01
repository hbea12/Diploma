package com.orvos.repositories;

import com.orvos.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer>{

    Patient findByTaj(String taj);
}
