package com.oalkan.patientservice.controller;

import com.oalkan.patientservice.model.Patient;
import com.oalkan.patientservice.model.dto.*;
import com.oalkan.patientservice.service.PatientService;
import healthcare.HospitalResponse;
import healthcare.HospitalResponse2;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/patient")
public class PatientController {
    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody PatientDTO patientDTO) {
        Optional<Patient> existingPatient = patientService.findByEmailOrPhone(patientDTO.getEmail(), patientDTO.getPhone());

        if (existingPatient.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Patient with given email or phone already exists.");
        }

        Patient patient = patientService.add(patientDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(patient);
    }

    @GetMapping
    public ResponseEntity<List<Patient>> getAll() {
        List<Patient> patients = patientService.getAll();
        return ResponseEntity.ok(patients);
    }

    @GetMapping("{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable int id) throws Exception {
        Patient patient = patientService.getById(id);

        if(patient == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(patient);
    }

    @PutMapping("{id}")
    public ResponseEntity<Patient> update(@PathVariable int id, @RequestBody PatientDTO patientDTO) {
        Patient updatedPatient = patientService.update(id, patientDTO);
        if (updatedPatient == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedPatient);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        boolean deleted = patientService.delete(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/hospitalcheck/{hospitalId}")
    public ResponseEntity<HospitalCheckResponseDTO> checkHospital2(@PathVariable int hospitalId) {
        HospitalResponse hospitalResponse = patientService.checkHospitalExists(hospitalId);
        return ResponseEntity.ok(
                HospitalCheckResponseDTO.builder()
                        .exist(hospitalResponse.getExists())
                        .build()
        );
    }

    @GetMapping("/getHospital/{hospitalId}")
    public ResponseEntity<HospitalResponseDTO> getHospital(@PathVariable int hospitalId) {
        HospitalResponse2 hospitalResponse = patientService.getHospital(hospitalId);
        return ResponseEntity.ok(
                HospitalResponseDTO.builder()
                        .name(hospitalResponse.getName())
                        .address(hospitalResponse.getAddress())
                        .phone(hospitalResponse.getPhone())
                        .capacity(hospitalResponse.getCapacity())
                        .build()
        );
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerHospital(@RequestBody HospitalPatientDTO hospitalPatientDTO) throws Exception {
        boolean exists = patientService.isRegistrationExists(hospitalPatientDTO.getPatientId(), hospitalPatientDTO.getHospitalId());

        if (exists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("This patient is already registered in this hospital.");
        }

        HospitalPatientDTO patientResponse = patientService.registerHospital(hospitalPatientDTO);
        if (patientResponse == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to register patient in hospital");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(patientResponse);
    }
}
