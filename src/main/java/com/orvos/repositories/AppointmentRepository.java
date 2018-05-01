package com.orvos.repositories;

import com.orvos.models.Appointment;
import com.orvos.models.Disease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer>{

}
