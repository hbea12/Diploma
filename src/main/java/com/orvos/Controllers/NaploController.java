package com.orvos.Controllers;

import com.orvos.models.Naplo;
import com.orvos.models.Orvos;
import com.orvos.repositories.GyogyszerRepository;
import com.orvos.repositories.OrvosRepository;
import com.orvos.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

@Controller
public class NaploController {

    private GyogyszerService gyogyszerService;
    private DiseaseService diseaseService;
    private PatientService patientService;
    private NaploService naploService;
    private OrvosService orvosService;
    private GyogyszerRepository gyogyszerRepository;
    private OrvosRepository orvosRepository;

    @Autowired
    public NaploController(GyogyszerService gyogyszerService, DiseaseService diseaseService, PatientService patientService, NaploService naploService, OrvosService orvosService, GyogyszerRepository gyogyszerRepository, OrvosRepository orvosRepository) {
        this.gyogyszerService = gyogyszerService;
        this.diseaseService = diseaseService;
        this.patientService = patientService;
        this.naploService = naploService;
        this.orvosService = orvosService;
        this.gyogyszerRepository = gyogyszerRepository;
        this.orvosRepository = orvosRepository;
    }

    private static final DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");


    @PostMapping("/ujnaplo")
    public String postNewPatient(@ModelAttribute Naplo naplo, Model model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        LocalDateTime now = LocalDateTime.now();
        Naplo naploToSave = new Naplo(naploService.getCalendar(now.getDayOfMonth(), now.getMonthValue(), now.getYear()),naplo.getTaj(), naplo.getBetegsegAzonosito(),
                naplo.getGyogzszerAzonosito(), naplo.getGyogyszerFelirtAdag(), naplo.getBetegPanasz());
        naploToSave.setGyogzszerNev(gyogyszerRepository.findOne(new Integer(naplo.getGyogzszerAzonosito())).getGyogyszerNev());
        naploToSave.setOrvosNev(orvosRepository.findByOrvosAzonosito(auth.getName()).getOrvosNev());

        Orvos orvos = orvosRepository.findByOrvosAzonosito(auth.getName());
        naploToSave.setOrvosAzonosito(orvos.getId().toString());
        naploService.saveNaplo(naploToSave);
        return "redirect:/";
    }




    @GetMapping("/ujnaplo")
    public String getNewPatient(Model model){
        Naplo naplo = new Naplo();
        model.addAttribute("betegsegList", diseaseService.getAllBetegseg());
        model.addAttribute("gyogzszerList", gyogyszerService.getAllGyogyszer());
        model.addAttribute(naplo);
        return "Naplo";
    }
}
