package com.orvos.Controllers;


import com.orvos.models.*;
import com.orvos.repositories.NaploRepository;
import com.orvos.repositories.OrvosRepository;
import com.orvos.repositories.RoleRepository;
import com.orvos.repositories.UserRepository;
import com.orvos.services.NaploService;
import com.orvos.services.OrvosService;
import com.orvos.services.PatientService;
import com.orvos.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.orvos.models.RoleEnum.ROLE_USER;

@Controller
public class PatientController {

    private PatientService patientService;
    private OrvosService orvosService;
    private OrvosRepository orvosRepository;
    private UserService userService;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private NaploService naploService;
    private NaploRepository naploRepository;

    @Autowired
    public PatientController(PatientService patientService, OrvosService orvosService, OrvosRepository orvosRepository, UserService userService, UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder, NaploService naploService, NaploRepository naploRepository) {
        this.patientService = patientService;
        this.orvosService = orvosService;
        this.orvosRepository = orvosRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.naploService = naploService;
        this.naploRepository = naploRepository;
    }

    @PostMapping("/ujbeteg")
    public String postNewPatient(@ModelAttribute Patient patient, Model model){

        if (patientService.findByTaj(patient.getTaj()) != null){
            model.addAttribute("betegIdFoglalt", true);
            return "Patient";
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Orvos orvos = orvosRepository.findByOrvosAzonosito(auth.getName());

        Set<Role> roleSet = new HashSet<>();
        Role roleUser = roleRepository.findByName(ROLE_USER.getRole());

        User user = new User(patient.getTaj(),passwordEncoder.encode("alma"), orvos);

        roleSet.add(roleUser);
        user.setRoles(roleSet);

        userRepository.save(user);
        patientService.saveBeteg(patient);
        return "redirect:/";
    }


    @GetMapping("/ujbeteg")
    public String getNewPatient(Model model){
        Patient patient = new Patient();
        model.addAttribute(patient);
        model.addAttribute("orvosList", orvosService.getAllOrvos());
        return "Patient";
    }

    @GetMapping("/betegnaplo")
    public String betegnaplo(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        model.addAttribute("betegNaplo", naploRepository.findByTaj(auth.getName()));
        return "BetegNaplo";
    }

    @GetMapping("/betegadatok")
    public String betegadatok(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Patient patient = patientService.findByTaj(auth.getName());
        System.out.println(patient);
        model.addAttribute(patient);
        return "EditPatient";
    }

    @PostMapping("/betegadatok")
    public String betegadatok(@ModelAttribute Patient patient){
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Patient patient1 = patientService.findByTaj(patient.getTaj());
        patient1.setCim(patient.getCim());
        patient1.setEmail(patient.getEmail());
        patient1.setNev(patient.getNev());

        patientService.saveBeteg(patient1);

        return "redirect:/";
    }

}
