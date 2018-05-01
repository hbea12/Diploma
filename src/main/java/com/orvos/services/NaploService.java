package com.orvos.services;

import com.orvos.models.Naplo;
import com.orvos.models.Orvos;
import com.orvos.repositories.NaploRepository;
import com.orvos.repositories.OrvosRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

@Slf4j
@Service
public class NaploService {


    private NaploRepository naploRepository;
    private OrvosRepository orvosRepository;

    @Autowired
    public NaploService(NaploRepository naploRepository, OrvosRepository orvosRepository) {
        this.naploRepository = naploRepository;
        this.orvosRepository = orvosRepository;
    }

    public Naplo saveNaplo(Naplo naplo){
        return naploRepository.save(naplo);
    }

    public String getCalendar(int day, int month, int year) {
        LocalDate date = LocalDate.of(year,month,day);
        return date.toString();
    }

    public Map<String, Integer> getFelirtGyogyszerek(String orvosAzonosito){
        Orvos orvos = orvosRepository.findByOrvosAzonosito(orvosAzonosito);
        List<Naplo> naploList = naploRepository.findByOrvosAzonosito(orvos.getId().toString());
        Map<String, Integer> felirtGyogyszerek = new HashMap<>();
        for (Naplo naplo : naploList){
            if (null == felirtGyogyszerek.get(naplo.getGyogzszerNev())){
                felirtGyogyszerek.put(naplo.getGyogzszerNev(), 1);
            } else {
                felirtGyogyszerek.put(naplo.getGyogzszerNev(), felirtGyogyszerek.get(naplo.getGyogzszerNev())+1);
            }
        }
        return sortByValue(felirtGyogyszerek);
    }

    private Map<String, Integer> sortByValue(Map<String, Integer> unsortMap) {
        Map<String, Integer> sorted = unsortMap
                .entrySet()
                .stream()
                .sorted(comparingByValue())
                .collect(
                        toMap(e -> e.getKey(), e -> e.getValue(),
                        (e1, e2) -> e2,
                        LinkedHashMap::new));

        sorted = sorted
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect( toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        return sorted;
    }
}
