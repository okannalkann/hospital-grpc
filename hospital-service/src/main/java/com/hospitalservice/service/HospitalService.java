package com.hospitalservice.service;

import com.hospitalservice.exception.HospitalNotFoundException;
import com.hospitalservice.model.dto.HospitalDTO;
import com.hospitalservice.model.Hospital;
import com.hospitalservice.repository.HospitalRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HospitalService {
    private final HospitalRepository hospitalRepository;

    @Transactional
    public Hospital update(int id, HospitalDTO hospitalDTO) {
        Hospital hospital = hospitalRepository.findById(id)
                .orElseThrow(() -> new HospitalNotFoundException("Hospital with ID " + id + " not found"));

        hospital.setName(hospitalDTO.getName());
        hospital.setAddress(hospitalDTO.getAddress());
        hospital.setPhone(hospitalDTO.getPhone());
        hospital.setCapacity(hospitalDTO.getCapacity());
        return hospitalRepository.save(hospital);
    }

    @Transactional
    public boolean delete(int id) {
        try {
            return hospitalRepository.findById(id)
                    .map(patient -> {
                        hospitalRepository.deleteById(id);
                        return true;
                    })
                    .orElseGet(() -> {
                        return false;
                    });
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to delete hospital", e);
        }
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

    public Hospital getById(int id) {
        return hospitalRepository.findById(id)
                .orElseThrow(() -> new HospitalNotFoundException("Hospital not found"));
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
