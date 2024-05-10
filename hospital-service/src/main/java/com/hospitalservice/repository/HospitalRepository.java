package com.hospitalservice.repository;

import com.hospitalservice.model.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HospitalRepository extends JpaRepository<Hospital, Integer> {
}
