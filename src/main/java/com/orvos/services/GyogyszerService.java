package com.orvos.services;

import com.orvos.models.Gyogyszer;
import com.orvos.repositories.GyogyszerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class GyogyszerService {

    private GyogyszerRepository gyogyszerRepository;

    @Autowired
    public GyogyszerService(GyogyszerRepository gyogyszerRepository) {
        this.gyogyszerRepository = gyogyszerRepository;
    }

    public Gyogyszer saveGyogyszer(Gyogyszer gyogyszer) {
        return gyogyszerRepository.save(gyogyszer);
    }

    public Gyogyszer findByGyogyszerAzonosito(String gyogyszerAzonosito) {
        return gyogyszerRepository.findByGyogyszerAzonosito(gyogyszerAzonosito);
    }

    public List<Gyogyszer> getAllGyogyszer() {
        return gyogyszerRepository.findAll();
    }

    public List<String> getAllHatoanyag(){
        List<String> hatoanyagList = new ArrayList<>();

        for (Gyogyszer gyogyszer: gyogyszerRepository.findAll()){
            if (!hatoanyagList.contains(gyogyszer.getGyogyszerHatoanyag())){
                hatoanyagList.add(gyogyszer.getGyogyszerHatoanyag());
            }
        }
        return hatoanyagList;
    }
}