package com.oalkan.patientservice.controller;

import com.oalkan.patientservice.exception.HospitalNotFoundException;
import com.oalkan.patientservice.model.Patient;
import com.oalkan.patientservice.model.dto.*;
import com.oalkan.patientservice.response.ApiResponse;
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
    public ResponseEntity<ApiResponse> create(@RequestBody PatientDTO patientDTO) {
        Optional<Patient> existingPatient = patientService.findByEmailOrPhone(patientDTO.getEmail(), patientDTO.getPhone());

        if (existingPatient.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(false, "Patient with given email or phone already exists.", null));
        }

        try {
            Patient patient = patientService.add(patientDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, "Patient created successfully", patient));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse(false, "Failed to create patient", null));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAll() {
        try {
            List<Patient> patients = patientService.getAll();
            if (patients.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse(true, "No patients found", patients));
            }
            return ResponseEntity.ok(new ApiResponse(true, "Patients retrieved successfully", patients));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse(false, "Failed to retrieve patients", null));
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse> getPatientById(@PathVariable int id) {
        try {
            Patient patient = patientService.getById(id);
            return ResponseEntity.ok(new ApiResponse(true, "Patient found", patient));
        } catch (HospitalNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Patient not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable int id, @RequestBody PatientDTO patientDTO) {
        try {
            Patient updatedPatient = patientService.update(id, patientDTO);
            return ResponseEntity.ok(new ApiResponse(true, "Patient updated", updatedPatient));
        } catch (HospitalNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Patient not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable int id) {
        boolean deleted = patientService.delete(id);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Patient not found"));
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Patient deleted successfully"));
    }

    @GetMapping("/hospitalcheck/{hospitalId}")
    public ResponseEntity<ApiResponse> checkHospitalExist(@PathVariable int hospitalId) {
        HospitalResponse hospitalResponse = patientService.checkHospitalExists(hospitalId);

        if (hospitalResponse == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Hospital not found"));
        }


        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(true, hospitalResponse.getExists() ? "Hospital exist" : "Hospital not exist" ));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerHospital(@RequestBody HospitalPatientDTO hospitalPatientDTO) throws Exception {
        Patient patient = patientService.findPatientById(hospitalPatientDTO.getPatientId());
        if (patient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Patient not found"));
        }

        HospitalResponse hospital = patientService.checkHospitalExists(hospitalPatientDTO.getHospitalId());
        if (!hospital.getExists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Hospital not found"));
        }

        boolean exists = patientService.isRegistrationExists(hospitalPatientDTO.getPatientId(), hospitalPatientDTO.getHospitalId());
        if (exists) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "This patient is already registered in this hospital."));
        }

        HospitalPatientDTO patientResponse = patientService.registerHospital(hospitalPatientDTO);
        if (patientResponse == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Unable to register patient in hospital."));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, "Patient is successfully saved", patientResponse));
    }

    @GetMapping("/getHospitalPatients/{hospitalId}")
    public ResponseEntity<ApiResponse> getHospitalPatients(@PathVariable int hospitalId) {
        HospitalResponse2 hospitalResponse = patientService.getHospital(hospitalId);

        if (hospitalResponse == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Hospital not found"));
        }

        List<PatientDTO> patients = patientService.getPatientsByHospitalId(hospitalId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(true, "Hospital patients pulled successfully", HospitalResponseDTO.builder()
                        .hospitalId(hospitalId)
                        .name(hospitalResponse.getName())
                        .address(hospitalResponse.getAddress())
                        .phone(hospitalResponse.getPhone())
                        .capacity(hospitalResponse.getCapacity())
                        .patients(patients)
                        .build()));
    }
}
