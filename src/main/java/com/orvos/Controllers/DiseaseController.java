package com.orvos.Controllers;

import com.orvos.models.Disease;
import com.orvos.repositories.DiseaseRepository;
import com.orvos.services.DiseaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DiseaseController {

    private DiseaseService diseaseService;
    private DiseaseRepository diseaseRepository;

    @Autowired

    public DiseaseController(DiseaseService diseaseService, DiseaseRepository diseaseRepository) {
        this.diseaseService = diseaseService;
        this.diseaseRepository = diseaseRepository;
    }

    @PostMapping("/ujbetegseg")
    public String postNewPatient(@ModelAttribute Disease disease, Model model){
        if (diseaseRepository.findByBetegsegId(disease.getBetegsegId()) != null){
            model.addAttribute("betegsegIdFoglalt", true);
            return "Disease";
        }
        diseaseService.saveBetegseg(disease);
        return "redirect:/";
    }


    @GetMapping("/ujbetegseg")
    public String getNewPatient(Model model){
        Disease disease = new Disease();
        model.addAttribute(disease);
        return "Disease";
    }
}
