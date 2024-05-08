package com.hospitalservice.service;

import com.hospitalservice.model.dto.HospitalDTO;
import com.hospitalservice.model.Hospital;
import com.hospitalservice.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HospitalService {
    private final HospitalRepository hospitalRepository;

    public Hospital add(HospitalDTO hospitalDTO) {
        Hospital hospital = convertToEntity(hospitalDTO);
        return hospitalRepository.save(hospital);
    }

    public List<Hospital> getAll() {
        return hospitalRepository.findAll();
    }

    private Hospital convertToEntity(HospitalDTO hospitalDTO) {
        Hospital hospital = new Hospital();
        hospital.setId(hospitalDTO.getId());
        hospital.setName(hospitalDTO.getName());
        hospital.setAddress(hospitalDTO.getAddress());
        hospital.setPhone(hospitalDTO.getPhone());
        hospital.setCapacity(hospitalDTO.getCapacity());
        return hospital;
    }
}
