package com.orvos.services;

import com.orvos.models.*;
import com.orvos.repositories.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.orvos.models.RoleEnum.ROLE_ADMIN;
import static com.orvos.models.RoleEnum.ROLE_USER;

@Slf4j
@Service
@Transactional
public class DataLoader {

    private OrvosRepository orvosRepository;
    private PatientRepository patientRepository;
    private NaploRepository naploRepository;
    private GyogyszerRepository gyogyszerRepository;
    private DiseaseRepository diseaseRepository;
    private UserRepository userRepository;
    private OrvosService orvosService;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private AppointmentRepository appointmentRepository;

    @Autowired
    public DataLoader(OrvosRepository orvosRepository, PatientRepository patientRepository, NaploRepository naploRepository, GyogyszerRepository gyogyszerRepository, DiseaseRepository diseaseRepository, UserRepository userRepository, OrvosService orvosService, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder, AppointmentRepository appointmentRepository) {
        this.orvosRepository = orvosRepository;
        this.patientRepository = patientRepository;
        this.naploRepository = naploRepository;
        this.gyogyszerRepository = gyogyszerRepository;
        this.diseaseRepository = diseaseRepository;
        this.userRepository = userRepository;
        this.orvosService = orvosService;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.appointmentRepository = appointmentRepository;
    }


    @PostConstruct
    public void loadData(){
        loadOrvos();
        loadBeteg();
        loadBetegseg();
        loadGyogyszer();
        loadNaplo();
    }

    private void loadOrvos(){
        Set<Role> roleSet = new HashSet<>();
        String morning = "8:00 - 12:30";
        String afternoon = "13:00 - 18:00";
        String holiday = "-";

        Appointment appointment = new Appointment(morning, holiday, morning,afternoon, holiday, afternoon, morning,afternoon,holiday,afternoon);
        Appointment appointment1 = new Appointment(holiday,afternoon,morning,afternoon,morning,afternoon,holiday,afternoon,morning,afternoon);
        appointmentRepository.save(appointment);
        appointmentRepository.save(appointment1);

        Orvos orvos = new Orvos("12345678","Kiss József", "2013 Pomáz", "Fő utca 12","+36301112233");
        orvos.setAppointment(appointment);
        orvos.setAppointmentVisible(true);
        orvos.setStatus(true);

        Orvos orvos1 = new Orvos("87654321", "Kovács Péter","2013 Pomáz", "Fő utca 12","+36302228833");
        orvos1.setAppointment(appointment1);
        orvos1.setAppointmentVisible(true);
        orvos1.setStatus(true);

        String userEncriptedPw = passwordEncoder.encode("alma");
        User user = new User(orvos.getOrvosAzonosito(),userEncriptedPw, orvos);
        user.setPassword2(userEncriptedPw);

        user.setDefaultPasswordChanged(false);

        String userEncriptedPw1 = passwordEncoder.encode("alma");
        User user1 = new User(orvos1.getOrvosAzonosito(),userEncriptedPw1, orvos1);
        user1.setPassword2(userEncriptedPw1);
        user1.setSamePassword(true);
        user1.setDefaultPasswordChanged(true);
        Role roleAdmin = new Role(ROLE_ADMIN.getRole());
        roleRepository.save(roleAdmin);
        roleSet.add(roleAdmin);
        user.setRoles(roleSet);
        user1.setRoles(roleSet);
        System.out.println(roleSet);

        orvosRepository.save(orvos);
        orvosRepository.save(orvos1);
        userRepository.save(user);
        userRepository.save(user1);
    }


    private void loadIdopont(){



    }

    private void loadBeteg(){
        Role roleUser = new Role(ROLE_USER.getRole());
        roleRepository.save(roleUser);

        Set<Role> roleSet = new HashSet<>();
        roleSet.add(roleUser);

        Patient patient = new Patient("123456789", "Gipsz Jakab", getCalendar(7,8,1983),
                "2014 Budakalász, Rózsa utca 2", "87654321", "gipsz.jakab@valami.hu");

        Patient patient1 = new Patient("987654321", "Tóth Béla", getCalendar(17,2,1971),
                "2013 Pomáz, Fő utca 48", "12345678", "toth.bela@valami.hu");

        Patient patient2 = new Patient("111222333", "Tóth Margit", getCalendar(18,4,1993),
                "2013 Pomáz, Fő utca 48", "12345678", "margit.toth@valami.hu");



        String userEncriptedPw = passwordEncoder.encode("alma");

        User user = new User(patient.getTaj(),userEncriptedPw, orvosRepository.findByOrvosAzonosito("87654321"));
        User user1 = new User(patient1.getTaj(),userEncriptedPw, orvosRepository.findByOrvosAzonosito("12345678"));
        User user2 = new User(patient2.getTaj(),userEncriptedPw, orvosRepository.findByOrvosAzonosito("12345678"));
        user.setDefaultPasswordChanged(true);
        user.setPassword2(userEncriptedPw);
        user.setSamePassword(true);
        user1.setDefaultPasswordChanged(true);
        user1.setPassword2(userEncriptedPw);
        user1.setSamePassword(true);
        user2.setDefaultPasswordChanged(true);
        user2.setPassword2(userEncriptedPw);
        user2.setSamePassword(true);

        user.setRoles(roleSet);
        user1.setRoles(roleSet);
        user2.setRoles(roleSet);


        patientRepository.save(patient);
        patientRepository.save(patient1);
        patientRepository.save(patient2);

        userRepository.save(user);
        userRepository.save(user1);
        userRepository.save(user2);



        userRepository.save(user);







    }

    private void loadBetegseg(){
        List<Disease> diseaseList = new ArrayList<>();
        diseaseList.add(new Disease("O123", "Felsőlégúti fertőzés"));
        diseaseList.add(new Disease("T321", "Tüdőgyulladás"));
        diseaseList.add(new Disease("I888", "Infulenza"));
        diseaseList.add(new Disease("M587", "Magas vérnyomás"));

        for (Disease disease : diseaseList){
            diseaseRepository.save(disease);
        }
    }

    public String getCalendar(int day, int month, int year) {
        LocalDate date = LocalDate.of(year,month,day);
        return date.toString();
    }

    private void loadGyogyszer(){
        List<Gyogyszer> gyogyszerList = new ArrayList<>();
        gyogyszerList.add(new Gyogyszer("0", "", "", 0, 0, "", false));
        gyogyszerList.add(new Gyogyszer("X1234", "ADVIL ULTRA FORTE", "Ibuprofen", 2020, 16, "Doboz", false));
        gyogyszerList.add(new Gyogyszer("X1235", "ADVIL ULTRA FORTE", "Ibuprofen", 2800, 24, "Doboz", false));
        gyogyszerList.add(new Gyogyszer("Y3211", "Nurofen 200 mg", "Ibuprofen", 910,12,"Doboz", false));
        gyogyszerList.add(new Gyogyszer("Y312", "Nurofen 400 mg", "Ibuprofen", 1200,12,"Doboz", false));
        gyogyszerList.add(new Gyogyszer("M1111", "Ebrit", "Irbesartan", 1530, 28, "Doboz", true));
        gyogyszerList.add(new Gyogyszer("N4444", "Neo citran", "Paracetamol", 2200,10,"Doboz",false));

        for (Gyogyszer gyogyszer : gyogyszerList){
            gyogyszerRepository.save(gyogyszer);
        }
    }

    private void loadNaplo(){
        List<Naplo> naploList = new ArrayList<>();
        new Naplo();
        naploList.add(new Naplo(getCalendar(5,1,2018), "123456789", diseaseRepository.findByBetegsegId("M587").getId().toString(), gyogyszerRepository.findByGyogyszerAzonosito("M1111").getId().toString(), "3x3", orvosRepository.findByOrvosAzonosito("87654321").getId().toString(), "Magas vérnyomás"));
        naploList.add(new Naplo(getCalendar(6,1,2018), "123456789", diseaseRepository.findByBetegsegId("I888").getId().toString(), gyogyszerRepository.findByGyogyszerAzonosito("N4444").getId().toString(), "3x3", orvosRepository.findByOrvosAzonosito("12345678").getId().toString(), "Magas láz, orr dugulás, köhögés"));
        naploList.add(new Naplo(getCalendar(7,1,2018), "987654321", diseaseRepository.findByBetegsegId("I888").getId().toString(), gyogyszerRepository.findByGyogyszerAzonosito("N4444").getId().toString(), "3x4", orvosRepository.findByOrvosAzonosito("12345678").getId().toString(),"Magas láz, orr dugulás"));
        naploList.add(new Naplo(getCalendar(8,1,2018), "111222333", diseaseRepository.findByBetegsegId("I888").getId().toString(), gyogyszerRepository.findByGyogyszerAzonosito("N4444").getId().toString(), "3x5", orvosRepository.findByOrvosAzonosito("87654321").getId().toString(), "Magas láz, orr dugulás"));

        for (Naplo naplo : naploList){
            naplo.setGyogzszerNev(gyogyszerRepository.findOne(new Integer(naplo.getGyogzszerAzonosito())).getGyogyszerNev());
            naplo.setOrvosNev(orvosService.getOrvosById(new Integer(naplo.getOrvosAzonosito())).getOrvosNev());
            naploRepository.save(naplo);
        }
    }

}
