package com.orvos.Controllers;

import com.orvos.models.Gyogyszer;
import com.orvos.repositories.GyogyszerRepository;
import com.orvos.services.GyogyszerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;

@Controller
public class GyogyszerController {

    private GyogyszerService gyogyszerService;
    private GyogyszerRepository gyogyszerRepository;

    @Autowired
    public GyogyszerController(GyogyszerService gyogyszerService, GyogyszerRepository gyogyszerRepository) {
        this.gyogyszerService = gyogyszerService;
        this.gyogyszerRepository = gyogyszerRepository;
    }

    @PostMapping("/ujgyogyszer")
    public String postNewPatient(@ModelAttribute Gyogyszer gyogyszer, Model model){
        if (gyogyszerRepository.findByGyogyszerAzonosito(gyogyszer.getGyogyszerAzonosito()) != null){
            model.addAttribute("invalidGyogyszerAzonosito", true);
            return "Gyogyszer";
        }

        gyogyszerRepository.save(gyogyszer);
        return "redirect:/";
    }


    @GetMapping("/ujgyogyszer")
    public String getNewPatient(Model model){
        Gyogyszer gyogyszer = new Gyogyszer();
        model.addAttribute(gyogyszer);
        model.addAttribute("gyogyszerEditable", false);
        model.addAttribute("venykoteles", Arrays.asList("Igen", "Nem"));
        return "Gyogyszer";
    }
}
