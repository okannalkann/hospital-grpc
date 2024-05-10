package com.oalkan.patientservice.service;

import com.oalkan.patientservice.model.*;
import com.oalkan.patientservice.model.dto.*;
import com.oalkan.patientservice.repository.HospitalPatientRepository;
import com.oalkan.patientservice.repository.PatientRepository;
import com.oalkan.patientservice.service.grpc.HospitalGrpcService;
import healthcare.HospitalRequest;
import healthcare.HospitalResponse;
import healthcare.HospitalResponse2;
import healthcare.HospitalServiceGrpc;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {
    private HospitalServiceGrpc.HospitalServiceBlockingStub hospitalServiceStub;

    private final PatientRepository patientRepository;
    private final HospitalPatientRepository hospitalPatientRepository;
    private final HospitalGrpcService hospitalGrpcService;

    public HospitalResponse checkHospitalExists(int hospitalId) {
        HospitalRequest hospitalRequest = HospitalRequest.newBuilder()
                .setHospitalId(hospitalId)
                .build();
        return hospitalGrpcService.checkHospitalExists(hospitalRequest);
    }

    public HospitalResponse2 getHospital(int hospitalId) {
        HospitalRequest hospitalRequest = HospitalRequest.newBuilder()
                .setHospitalId(hospitalId)
                .build();
        return hospitalGrpcService.GetHospital(hospitalRequest);
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

    public Optional<Patient> findByEmailOrPhone(String email, String phone) {
        return patientRepository.findByEmailOrPhone(email, phone);
    }

    public boolean isRegistrationExists(int patientId, int hospitalId) {
        return hospitalPatientRepository.findByPatientIdAndHospitalId(patientId, hospitalId).isPresent();
    }


    @Transactional
    public Patient update(int id, PatientDTO patientDTO) {
        Optional<Patient> patientOptional = patientRepository.findById(id);

        if (patientOptional.isPresent()) {
            Patient patient = patientOptional.get();
            patient.setAddress(patientDTO.getAddress());
            patient.setPhone(patientDTO.getPhone());
            patient.setDateofbirth(patientDTO.getDateofbirth());
            patient.setFirstname(patientDTO.getFirstname());
            patient.setLastname(patientDTO.getLastname());
            patient.setGender(patientDTO.getGender());
            patient.setEmail(patientDTO.getEmail());
            patient.setEmergencycontact(patientDTO.getEmergencycontact());
            return patientRepository.save(patient);
        }

        return null;
    }

    public Patient add(PatientDTO patientDTO) {
        Patient patient = convertToEntity(patientDTO);
        return patientRepository.save(patient);
    }

    @Transactional
    public boolean delete(int id) {
        Optional<Patient> patient = patientRepository.findById(id);
        if (patient.isPresent()) {
            patientRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private Patient convertToEntity(PatientDTO patientDTO) {
        try {
            Patient patient = new Patient();
            patient.setAddress(patientDTO.getAddress());;
            patient.setDateofbirth(patientDTO.getDateofbirth());
            patient.setPhone(patientDTO.getPhone());
            patient.setFirstname(patientDTO.getFirstname());
            patient.setLastname(patientDTO.getLastname());
            patient.setGender(patientDTO.getGender());
            patient.setEmail(patientDTO.getEmail());
            patient.setEmergencycontact(patientDTO.getEmergencycontact());
            return patient;
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to save patient", e);
        }
    }

    private HospitalResponseDTO convertToHospitalEntity(HospitalResponse2 hospitalResponse2) {
        try {
            return new HospitalResponseDTO(hospitalResponse2.getHospitalId(), hospitalResponse2.getName(), hospitalResponse2.getAddress(), hospitalResponse2.getPhone(), hospitalResponse2.getCapacity());
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to save patient", e);
        }
    }

    private HospitalPatient convertToHospitalPatientDto(HospitalPatientDTO hospitalpatientDTO) {
        try {
            Patient patient = getById(hospitalpatientDTO.getPatientId());

            HospitalResponse2 getHospital = getHospital(hospitalpatientDTO.getHospitalId());
            HospitalResponseDTO hospital = convertToHospitalEntity(getHospital);

            HospitalPatient hospitalpatient = new HospitalPatient();
            hospitalpatient.setId(hospitalpatientDTO.getId());
            hospitalpatient.setHospitalId(getHospital.getHospitalId());
            hospitalpatient.setPatient(patient);
            hospitalpatient.setDateRegistered(hospitalpatientDTO.getDateRegistered());
            hospitalpatient.setDateDischarged(hospitalpatientDTO.getDateDischarged());

            return hospitalpatient;
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to save patient", e);
        }
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

        HospitalResponse2 getHospital = getHospital(hospitalPatientDTO.getHospitalId());

        HospitalPatient hospitalpatient = convertToHospitalPatientDto(hospitalPatientDTO);
        hospitalPatientRepository.save(hospitalpatient);
        return hospitalPatientDTO;
    }
}
