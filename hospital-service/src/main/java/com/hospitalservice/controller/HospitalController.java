package com.hospitalservice.controller;

import com.hospitalservice.model.dto.HospitalDTO;
import com.hospitalservice.model.Hospital;
import com.hospitalservice.service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/hospital")
public class HospitalController {

    private final HospitalService hospitalService;

    @PostMapping
    public ResponseEntity<Hospital> create(@RequestBody HospitalDTO hospitalDTO) {
        Hospital hospital = hospitalService.add(hospitalDTO);
        return ResponseEntity.status(201).body(hospital);
    }

    @GetMapping
    public ResponseEntity<List<Hospital>> getAll() {
        List<Hospital> hospitals = hospitalService.getAll();
        return ResponseEntity.ok(hospitals);
    }

    @GetMapping("{id}")
    public ResponseEntity<HospitalDTO> getHospital(@PathVariable int id) {
        HospitalDTO hospital = hospitalService.getById(id);
        return ResponseEntity.ok(hospital);
    }


    @PutMapping("{id}")
    public ResponseEntity<Hospital> update(@PathVariable int id, @RequestBody HospitalDTO hospitalDTO) {
        Hospital updatedHospital = hospitalService.update(id, hospitalDTO);
        if (updatedHospital == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedHospital);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        boolean deleted = hospitalService.delete(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }
}
