package com.oalkan.patientservice.repository;

import com.oalkan.patientservice.model.HospitalPatient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HospitalPatientRepository extends JpaRepository<HospitalPatient, Integer> {
}
