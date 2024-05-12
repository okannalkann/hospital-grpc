package com.oalkan.patientservice.repository;

import com.oalkan.patientservice.model.HospitalPatient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HospitalPatientRepository extends JpaRepository<HospitalPatient, Integer> {
    Optional<HospitalPatient> findByPatientIdAndHospitalId(int patientId, int hospitalId);
    List<HospitalPatient> findByHospitalId(int hospitalId);
}
