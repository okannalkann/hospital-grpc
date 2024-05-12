package com.oalkan.patientservice.repository;

import com.oalkan.patientservice.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PatientRepository extends JpaRepository<Patient, Integer> {
    Optional<Patient> findByEmailOrPhone(String email, String phone);
}
