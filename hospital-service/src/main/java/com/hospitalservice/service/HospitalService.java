package com.hospitalservice.service;

import com.hospitalservice.model.dto.HospitalDTO;
import com.hospitalservice.model.Hospital;
import com.hospitalservice.repository.HospitalRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HospitalService {
    private final HospitalRepository hospitalRepository;

    @Transactional
    public Hospital update(int id, HospitalDTO hospitalDTO) {
        Optional<Hospital> hospitalOptional = hospitalRepository.findById(id);

        if (hospitalOptional.isPresent()) {
            Hospital hospital = hospitalOptional.get();
            hospital.setName(hospitalDTO.getName());
            hospital.setAddress(hospitalDTO.getAddress());
            hospital.setPhone(hospitalDTO.getPhone());
            hospital.setCapacity(hospitalDTO.getCapacity());
            return hospitalRepository.save(hospital);
        }

        return null;
    }

    @Transactional
    public boolean delete(int id) {
        Optional<Hospital> hospital = hospitalRepository.findById(id);
        if (hospital.isPresent()) {
            hospitalRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Hospital add(HospitalDTO hospitalDTO) {
        Hospital hospital = convertToEntity(hospitalDTO);
        return hospitalRepository.save(hospital);
    }

    public List<Hospital> getAll() {
        try {
            return hospitalRepository.findAll();
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to retrieve hospitals", e);
        }
    }

    public HospitalDTO getById(int id) {
        return null;
    }

    private Hospital convertToEntity(HospitalDTO hospitalDTO) {
        try {
            Hospital hospital = new Hospital();
            hospital.setName(hospitalDTO.getName());
            hospital.setAddress(hospitalDTO.getAddress());
            hospital.setPhone(hospitalDTO.getPhone());
            hospital.setCapacity(hospitalDTO.getCapacity());
            return hospital;
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to save hospital", e);
        }
    }
}
