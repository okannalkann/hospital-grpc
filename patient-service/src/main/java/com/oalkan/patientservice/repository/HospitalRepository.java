package com.oalkan.patientservice.repository;

import com.oalkan.patientservice.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HospitalRepository extends JpaRepository<Hospital, Integer> {
    Optional<Hospital> findByHospitalId(Integer hospitalId);
}
