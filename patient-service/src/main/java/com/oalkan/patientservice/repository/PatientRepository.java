package com.oalkan.patientservice.repository;

import com.oalkan.patientservice.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PatientRepository extends JpaRepository<Patient, Integer> {
}
