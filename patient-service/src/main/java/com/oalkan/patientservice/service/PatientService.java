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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private Hospital convertToHospitalEntity(HospitalResponse2 hospitalResponse2) {
        try {
            Hospital hospital = new Hospital();
            hospital.setName(hospitalResponse2.getName());
            hospital.setAddress(hospitalResponse2.getAddress());
            hospital.setPhone(hospitalResponse2.getPhone());
            hospital.setCapacity(hospitalResponse2.getCapactiy());

            return hospital;
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to save patient", e);
        }
    }


    private HospitalPatient convertToHospitalPatientDto(HospitalPatientDTO hospitalpatientDTO) {
        try {
            Patient patient = getById(hospitalpatientDTO.getPatient());

            HospitalResponse2 getHospital = getHospital(hospitalpatientDTO.getHospital());
            Hospital hospital = convertToHospitalEntity(getHospital);

            HospitalPatient hospitalpatient = new HospitalPatient();
            hospitalpatient.setId(hospitalpatientDTO.getId());
            hospitalpatient.setHospital(hospital);
            hospitalpatient.setPatient(patient);
            hospitalpatient.setDateRegistered(hospitalpatientDTO.getDateRegistered());
            hospitalpatient.setDateDischarged(hospitalpatientDTO.getDateDischarged());

            return hospitalpatient;
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to save patient", e);
        }
    }

    public HospitalPatientDTO registerHospital(HospitalPatientDTO hospitalPatientDTO){
        Patient patient = getById(hospitalPatientDTO.getPatient());
        HospitalResponse checkHospital = checkHospitalExists(hospitalPatientDTO.getHospital());

        if(checkHospital.getExists() && patient != null){
            //patient mevcutsa kontrolü, mevcut olduğunu bildir
             System.out.print(patient.getHospitalsVisited());
            //hospital tablosuna da de hastayı ekleme
            HospitalPatient hospitalpatient = convertToHospitalPatientDto(hospitalPatientDTO);
            hospitalPatientRepository.save(hospitalpatient);
            return hospitalPatientDTO;
        }else{
            return null; //patient ya da hastane yok
        }
    }
}
