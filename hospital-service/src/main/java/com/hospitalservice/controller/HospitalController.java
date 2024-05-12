package com.hospitalservice.controller;

import com.hospitalservice.exception.HospitalNotFoundException;
import com.hospitalservice.model.dto.HospitalDTO;
import com.hospitalservice.model.Hospital;
import com.hospitalservice.response.ApiResponse;
import com.hospitalservice.service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/hospital")
public class HospitalController {

    private final HospitalService hospitalService;

    @PostMapping
    public ResponseEntity<ApiResponse> create(@RequestBody HospitalDTO hospitalDTO) {
        try {
            Hospital hospital = hospitalService.add(hospitalDTO);
            return ResponseEntity.ok(new ApiResponse(true, "Hospital updated", hospital));
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "Failed to create hospital"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "Internal server error"));
        }
    }

    @GetMapping
    public ResponseEntity<List<Hospital>> getAll() {
        List<Hospital> hospitals = hospitalService.getAll();
        return ResponseEntity.ok(hospitals);
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse> getHospitalById(@PathVariable int id) {
        try {
            Hospital hospital = hospitalService.getById(id);
            return ResponseEntity.ok(new ApiResponse(true, "Hospital found", hospital));
        } catch (HospitalNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Hospital not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Internal server error"));
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable int id, @RequestBody HospitalDTO hospitalDTO) {
        try {
            Hospital updatedHospital = hospitalService.update(id, hospitalDTO);
            return ResponseEntity.ok(new ApiResponse(true, "Hospital updated", updatedHospital));
        } catch (HospitalNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Hospital not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Internal server error"));
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable int id) {

        boolean deleted = hospitalService.delete(id);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Hospital not found"));
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Hospital deleted successfully"));
    }
}
