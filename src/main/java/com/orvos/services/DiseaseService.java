package com.orvos.services;

import com.orvos.models.Disease;
import com.orvos.repositories.DiseaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DiseaseService {

    @Autowired
    private DiseaseRepository diseaseRepository;

    public Disease saveBetegseg(Disease disease){
        return diseaseRepository.save(disease);
    }

    public List<Disease> getAllBetegseg(){
        return diseaseRepository.findAll();
    }

/*    public Disease findByBetegsegAzonosito(String betegsegAzonosito){
        return diseaseRepository.findByBetegsegAzonosito(betegsegAzonosito);
    }*/

}
