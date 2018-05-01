package com.orvos.services;

import com.orvos.models.Patient;
import com.orvos.repositories.PatientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public Patient saveBeteg(Patient patient){
        return patientRepository.save(patient);
    }

    public Patient findByTaj(String taj) {return patientRepository.findByTaj(taj);}
}
