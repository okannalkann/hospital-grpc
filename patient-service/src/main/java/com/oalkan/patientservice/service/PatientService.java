package com.oalkan.patientservice.service;

import com.oalkan.patientservice.exception.EntityNotFoundException;
import com.oalkan.patientservice.model.*;
import com.oalkan.patientservice.model.dto.*;
import com.oalkan.patientservice.repository.HospitalPatientRepository;
import com.oalkan.patientservice.repository.PatientRepository;
import com.oalkan.patientservice.service.grpc.HospitalGrpcService;
import healthcare.HospitalRequest;
import healthcare.HospitalResponse;
import healthcare.HospitalResponse2;
import healthcare.HospitalServiceGrpc;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final HospitalPatientRepository hospitalPatientRepository;
    private final HospitalGrpcService hospitalGrpcService;

    public HospitalResponse checkHospitalExists(int hospitalId) {
        try {
            HospitalRequest hospitalRequest = HospitalRequest.newBuilder()
                    .setHospitalId(hospitalId)
                    .build();
            return hospitalGrpcService.checkHospitalExists(hospitalRequest);
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to check hospital", e);
        }
    }

    public HospitalResponse2 getHospital(int hospitalId) {
        try {
            HospitalRequest hospitalRequest = HospitalRequest.newBuilder()
                    .setHospitalId(hospitalId)
                    .build();

            return hospitalGrpcService.GetHospital(hospitalRequest);
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to retrieve hospitals", e);
        }
    }

    public List<Patient> getAll() {
        try {
            return patientRepository.findAll();
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to retrieve hospitals", e);
        }
    }

    public Patient getById(int id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    public Patient findPatientById(int id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    public List<PatientDTO> getPatientsByHospitalId(int hospitalId) {
        try {
            List<HospitalPatient> hospitalPatients = hospitalPatientRepository.findByHospitalId(hospitalId);
            return hospitalPatients.stream()
                    .map(hospitalPatient -> findPatientById(hospitalPatient.getId()))
                    .filter(Objects::nonNull)
                    .map(this::convertToPatientDTO)
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            throw new RuntimeException("Hospital not found", e);
        }
    }

    private PatientDTO convertToPatientDTO(Patient patient) {
        return PatientDTO.builder()
                .id(patient.getId())
                .firstname(patient.getFirstname())
                .lastname(patient.getLastname())
                .dateofbirth(patient.getDateofbirth())
                .gender(patient.getGender())
                .address(patient.getAddress())
                .phone(patient.getPhone())
                .email(patient.getEmail())
                .emergencycontact(patient.getEmergencycontact())
                .build();
    }

    public Optional<Patient> findByEmailOrPhone(String email, String phone) {
        try {
            return patientRepository.findByEmailOrPhone(email, phone);
        } catch (DataAccessException e) {
            throw new RuntimeException("Database operation failed while finding patient by email or phone", e);
        }
    }

    public boolean isRegistrationExists(int patientId, int hospitalId) {
        try {
            return hospitalPatientRepository.findByPatientIdAndHospitalId(patientId, hospitalId).isPresent();
        } catch (DataAccessException e) {
            throw new RuntimeException("Database operation failed while checking if registration exists", e);
        }
    }

    public Patient update(int id, PatientDTO patientDTO) {
        if (patientDTO == null) {
            throw new IllegalArgumentException("PatientDTO must not be null");
        }

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with ID: " + id));

        updatePatientFields(patient, patientDTO);
        try {
            return patientRepository.save(patient);
        } catch (DataAccessException e) {
            throw new RuntimeException("Database operation failed while updating patient", e);
        }
    }

    private void updatePatientFields(Patient patient, PatientDTO patientDTO) {
        patient.setFirstname(patientDTO.getFirstname());
        patient.setLastname(patientDTO.getLastname());
        patient.setDateofbirth(patientDTO.getDateofbirth());
        patient.setGender(patientDTO.getGender());
        patient.setAddress(patientDTO.getAddress());
        patient.setPhone(patientDTO.getPhone());
        patient.setEmail(patientDTO.getEmail());
        patient.setEmergencycontact(patientDTO.getEmergencycontact());
    }

    public Patient add(PatientDTO patientDTO) {
        if (patientDTO == null) {
            throw new IllegalArgumentException("PatientDTO must not be null");
        }

        try {
            Patient patient = convertToEntity(patientDTO);
            return patientRepository.save(patient);
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to save patient", e);
        }
    }

    public boolean delete(int id) {
        try {
            return patientRepository.findById(id)
                    .map(patient -> {
                        patientRepository.deleteById(id);
                        return true;
                    })
                    .orElseGet(() -> {
                        return false;
                    });
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to delete patient", e);
        }
    }

    private Patient convertToEntity(PatientDTO patientDTO) {
            Patient patient = new Patient();
            patient.setAddress(patientDTO.getAddress());
            patient.setDateofbirth(patientDTO.getDateofbirth());
            patient.setPhone(patientDTO.getPhone());
            patient.setFirstname(patientDTO.getFirstname());
            patient.setLastname(patientDTO.getLastname());
            patient.setGender(patientDTO.getGender());
            patient.setEmail(patientDTO.getEmail());
            patient.setEmergencycontact(patientDTO.getEmergencycontact());
            return patient;
    }

    private HospitalPatient convertToHospitalPatient(HospitalPatientDTO hospitalpatientDTO) {
            Patient patient = getById(hospitalpatientDTO.getPatientId());
            HospitalResponse2 getHospital = getHospital(hospitalpatientDTO.getHospitalId());

            HospitalPatient hospitalpatient = new HospitalPatient();
            hospitalpatient.setHospitalId(getHospital.getHospitalId());
            hospitalpatient.setPatient(patient);
            hospitalpatient.setDateRegistered(hospitalpatientDTO.getDateRegistered());
            hospitalpatient.setDateDischarged(hospitalpatientDTO.getDateDischarged());

            return hospitalpatient;
    }

    public HospitalPatientDTO registerHospital(HospitalPatientDTO hospitalPatientDTO) throws Exception{
        Patient patient = getById(hospitalPatientDTO.getPatientId());
        if(patient == null){
                throw new Exception("Patient not found with ID: " + hospitalPatientDTO.getPatientId());
        }
        HospitalResponse checkHospital = checkHospitalExists(hospitalPatientDTO.getHospitalId());
        if (!checkHospital.getExists()) {
            throw new Exception("Hospital not found with ID: " + hospitalPatientDTO.getHospitalId());
        }

        HospitalPatient hospitalpatient = convertToHospitalPatient(hospitalPatientDTO);
        hospitalPatientRepository.save(hospitalpatient);

        return hospitalPatientDTO;
    }
}
