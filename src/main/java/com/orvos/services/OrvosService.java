package com.orvos.services;

import com.orvos.models.Orvos;
import com.orvos.repositories.OrvosRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OrvosService {

    private OrvosRepository orvosRepository;

    @Autowired
    public OrvosService(OrvosRepository orvosRepository) {
        this.orvosRepository = orvosRepository;
    }

    public List<Orvos> getAllOrvos() {
        return orvosRepository.findAll();

    }

    public Orvos getOrvosById(Integer id){
        return orvosRepository.findOne(id);
    }
}