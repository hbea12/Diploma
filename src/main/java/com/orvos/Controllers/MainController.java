package com.orvos.Controllers;

import com.orvos.models.*;
import com.orvos.models.forms.Idoszak;
import com.orvos.repositories.*;
import com.orvos.services.GyogyszerService;
import com.orvos.services.NaploService;
import com.orvos.services.OrvosService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

import static com.orvos.models.RoleEnum.ROLE_ADMIN;
import static com.orvos.models.RoleEnum.ROLE_ANONYMOUS;

@Slf4j
@Controller
public class MainController {


    private GyogyszerRepository gyogyszerRepository;
    private GyogyszerService gyogyszerService;
    private PatientRepository betegService;
    private NaploRepository naploRepository;
    private OrvosRepository orvosRepository;
    private OrvosService orvosService;
    private UserRepository userRepository;
    private NaploService naploService;
    private RoleRepository roleRepository;
    private AppointmentRepository appointmentRepository;
    private BCryptPasswordEncoder passwordEncoder;


    @Autowired
    public MainController(GyogyszerRepository gyogyszerRepository, GyogyszerService gyogyszerService, PatientRepository betegService, NaploRepository naploRepository, OrvosRepository orvosRepository, OrvosService orvosService, UserRepository userRepository, NaploService naploService, RoleRepository roleRepository, AppointmentRepository appointmentRepository, BCryptPasswordEncoder passwordEncoder) {
        this.gyogyszerRepository = gyogyszerRepository;
        this.gyogyszerService = gyogyszerService;
        this.betegService = betegService;
        this.naploRepository = naploRepository;
        this.orvosRepository = orvosRepository;
        this.orvosService = orvosService;
        this.userRepository = userRepository;
        this.naploService = naploService;
        this.roleRepository = roleRepository;
        this.appointmentRepository = appointmentRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping("/")
    public String getIndex(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!auth.getPrincipal().equals("anonymousUser")) {
            User user = userRepository.findByUserName(auth.getName());

            if (null != user.getSamePassword() && user.getSamePassword() == false){
                model.addAttribute("notSamePassword", true);
            }

            if (!user.getDefaultPasswordChanged()) {
                user.setPassword("");
                model.addAttribute(user);
                model.addAttribute("DefaultPasswordChanged", false);
            }
        }

        model.addAttribute("orvosList", orvosRepository.findAll());
        return "index";
    }

   @PostMapping("/")
    public String postIndex(@ModelAttribute User user, Model model){

        User userPWToChange = userRepository.findByUserName(user.getUserName());

       if (!user.getPassword().equals(user.getPassword2())){
           userPWToChange.setSamePassword(false);
           userRepository.save(userPWToChange);

           return "redirect:/";
       }
       if (passwordEncoder.matches(user.getPassword(), userPWToChange.getPassword())){
          model.addAttribute("samePassword", true);
           return "redirect:/";
       }

       String encriptedPW = passwordEncoder.encode(user.getPassword());
        userPWToChange.setPassword(encriptedPW);
        userPWToChange.setPassword2(encriptedPW);
        userPWToChange.setSamePassword(true);
        userPWToChange.setDefaultPasswordChanged(true);
        userRepository.save(userPWToChange);
        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }


    @PostMapping("/hatoanyag")
    public String hatoanyag(@ModelAttribute Gyogyszer gyogyszer, Model model){

        List<Gyogyszer> filteredGyogyszerek =
                gyogyszerRepository.findByGyogyszerHatoanyag(gyogyszer.getGyogyszerHatoanyag());
        model.addAttribute("filteredGyogyszerek",filteredGyogyszerek);
        System.out.println(filteredGyogyszerek.toString());
        return "Gyogyszerek";
    }

    @GetMapping("/hatoanyag")
    public String hatoanyag(Model model){
        Gyogyszer gyogyszer = new Gyogyszer();
        List<String> gyogyszerList = gyogyszerService.getAllHatoanyag();

        Iterator itr = gyogyszerList.iterator();

        while (itr.hasNext())
        {
            if (itr.next().equals(""))
                itr.remove();
        }
        System.out.println(gyogyszerList);
        model.addAttribute(gyogyszer);
        model.addAttribute("hatoanyagList", gyogyszerList);
        return "Hatoanyag";
    }


    @PostMapping("/beteglekerdez")
    public String beteglekerdez(@ModelAttribute Patient patient, Model model){
        List<Naplo> betegNaplo = naploRepository.findByTaj(patient.getTaj());

        model.addAttribute("betegNaplo", betegNaplo);
        return "BetegNaplo";
    }


    @GetMapping("/beteglekerdez")
    public String beteglekerdez(Model model){
        Patient beteg = new Patient();
        model.addAttribute(beteg);
        return "BetegLekerdez";
    }

    @PostMapping("/regisztracio")
    public String regisztracio(@ModelAttribute User user, Model model){
        Set<Role> roleSet = new HashSet<>();
        Role role = roleRepository.findByName(ROLE_ADMIN.toString());
        Orvos orvos = new Orvos(user.getUserName(), user.getOrvos().getOrvosNev(),
                user.getOrvos().getZippAndCity(), user.getOrvos().getStreet(), user.getOrvos().getPhoneNumber());
        if (orvosRepository.findByOrvosAzonosito(orvos.getOrvosAzonosito()) != null){
            model.addAttribute("orvosIdFoglalt", true);
            return "Registration";
        }
        orvos.setStatus(true);
        roleSet.add(role);
        user.setRoles(roleSet);
        orvosRepository.save(orvos);

        String encriptedPW = passwordEncoder.encode("alma");

        user.setSamePassword(true);
        user.setDefaultPasswordChanged(false);
        user.setPassword(encriptedPW);
        user.setPassword2(encriptedPW);
        user.setOrvos(orvos);
        userRepository.save(user);
        //model.addAttribute("success", true);
        return "redirect:/";
    }


    @GetMapping("/regisztracio")
    public String regisztracio(Model model){
        User user = new User();
        model.addAttribute(user);
        return "Registration";
    }

    @PostMapping("/beteggyakorisag")
    public String betegGyakorisag(@ModelAttribute Idoszak idoszak, Model model){
        List<Naplo> betweenDateRange = naploRepository.findByLatogatasDatumBetween(idoszak.getFromDate(), idoszak.getToDate());

        model.addAttribute("betegek", betweenDateRange);
        model.addAttribute("betegekSyama", betweenDateRange.size());
        return "BetweenDate";
    }

    @GetMapping("/beteggyakorisag")
    public String betegGyakorisag(Model model){
        Idoszak idoszak = new Idoszak();
        model.addAttribute("idoszak", idoszak);
        return "Idoszak";
    }


    @GetMapping("/felirtgyogyszer")
    public String felirtGyogyszer(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Map<String, Integer> felirtGyogyszerMap = naploService.getFelirtGyogyszerek(auth.getName());

        Iterator<Map.Entry<String, Integer>> itr = felirtGyogyszerMap.entrySet().iterator();
        while (itr.hasNext())
        {
            Map.Entry<String, Integer> entry = itr.next();
            if (entry.getKey().equals(""))
                itr.remove();
        }

        model.addAttribute("felirtGyogyszerMap", felirtGyogyszerMap);
        return "FelirtGyogyszerek";
    }

    @GetMapping("/jelszocsere")
    public String passwordChange(Model model, HttpSession session){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByUserName(auth.getName());
        if (null != user.getSamePassword() && user.getSamePassword() == false){
            model.addAttribute("notSamePassword", true);
        }

        model.addAttribute(user);
        if (null != session.getAttribute("passwordChanged") &&
                session.getAttribute("passwordChanged").equals(true)
                && user.getSamePassword()){
            model.addAttribute("passwordChanged", true);
        }

        return "Password";
    }

    @PostMapping("/jelszocsere")
    public String passwordChange(@ModelAttribute User user, HttpSession session, Model model){

        User userPWToChange = userRepository.findByUserName(user.getUserName());

        if (!user.getPassword().equals(user.getPassword2())){
            userPWToChange.setSamePassword(false);
            userRepository.save(userPWToChange);
            return "redirect:/jelszocsere";
        }

        if (passwordEncoder.matches(user.getPassword(), userPWToChange.getPassword())){
            model.addAttribute("samePassword", true);
            return "redirect:/jelszocsere";
        }

        String encriptedPW = passwordEncoder.encode(user.getPassword());
        userPWToChange.setPassword(encriptedPW);
        userPWToChange.setPassword2(encriptedPW);
        userPWToChange.setSamePassword(true);
        userPWToChange.setDefaultPasswordChanged(true);
        userRepository.save(userPWToChange);
        session.setAttribute("passwordChanged", true);
        return "redirect:/";
    }


    @PostMapping("/orvosadat")
    public String orvosAdat(@ModelAttribute Orvos orvos, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Orvos orvos1 = orvosRepository.findByOrvosAzonosito(orvos.getOrvosAzonosito());
        orvos1.setPhoneNumber(orvos.getPhoneNumber());
        orvos1.setStreet(orvos.getStreet());
        orvos1.setZippAndCity(orvos.getZippAndCity());
        orvos1.setOrvosNev(orvos.getOrvosNev());
        orvosRepository.save(orvos1);

        return "redirect:/";
    }

    @GetMapping("/orvosadat")
    public String orvosAdat(Model model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Orvos orvos = orvosRepository.findByOrvosAzonosito(auth.getName());
        System.out.println(orvos);

        model.addAttribute(orvos);
        return "Orvos";
    }

    @GetMapping("/idopontkezeles")
    public String orvosIdopontkezeles(Model model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Orvos orvos = orvosRepository.findByOrvosAzonosito(auth.getName());

        model.addAttribute(orvos);
        return "Appointment";
    }

    @PostMapping("/idopontkezeles")
    public String orvosAdat(@ModelAttribute Orvos orvos){


        appointmentRepository.save(orvos.getAppointment());
        Orvos orvos1 = orvosRepository.findOne(orvos.getId());
        orvos1.setAppointment(orvos.getAppointment());
        orvos1.setAppointmentVisible(true);
        orvosRepository.save(orvos1);
        return "redirect:/";
    }


    @PostMapping("/gyogyszervalaszto")
    public String gyogyszerValaszto(@ModelAttribute Gyogyszer gyogyszer){
        return "redirect:/gyogyszerdata/?key="+ gyogyszer.getGyogyszerAzonosito();
    }

    @GetMapping("/gyogyszervalaszto")
    public String gyogyszerValaszto(Model model){
        Gyogyszer gyogyszer = new Gyogyszer();
        List<Gyogyszer> gyogyszerList = gyogyszerRepository.findAll();

        gyogyszerList.removeIf(i -> i.getGyogyszerAzonosito().equals("0"));
        model.addAttribute(gyogyszer);
        model.addAttribute(gyogyszerList);
        return "GyogyszerValaszto";
    }

    @GetMapping("/gyogyszerdata")
    public String gyogyszerData(@RequestParam(value="key") String gyogyszerAzonosito, Model model){

        Gyogyszer gyogyszer = gyogyszerRepository.findByGyogyszerAzonosito(gyogyszerAzonosito);
        model.addAttribute("gyogyszerEditable", true);
        model.addAttribute(gyogyszer);
        return "EditGyogyszer";
    }

    @PostMapping("/gyogyszerdata")
    public String gyogyszerData(@ModelAttribute Gyogyszer gyogyszer){
        System.out.println(gyogyszer);
        gyogyszerRepository.save(gyogyszer);
        return "redirect:/";
    }

    @GetMapping("/orvosstatus")
    public String orvosStatus( Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Orvos orvos = orvosRepository.findByOrvosAzonosito(auth.getName());

        model.addAttribute(orvos);
        return "OrvosStatus";
    }

    @PostMapping("/orvosstatus")
    public String orvosStatus(@ModelAttribute Orvos orvos){
        System.out.println(orvos);
        Orvos orvos1 = orvosRepository.findByOrvosAzonosito(orvos.getOrvosAzonosito());
        orvos1.setStatus(orvos.getStatus());
        orvosRepository.save(orvos1);
        return "redirect:/";
    }

}
