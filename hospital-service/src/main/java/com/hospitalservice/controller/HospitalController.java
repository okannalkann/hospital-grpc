package com.hospitalservice.controller;

import com.hospitalservice.model.dto.HospitalDTO;
import com.hospitalservice.model.Hospital;
import com.hospitalservice.service.HospitalService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<Hospital> create(@RequestBody HospitalDTO hospital){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        hospitalService.add(hospital)
                );
    }

    @GetMapping
    public ResponseEntity<List<Hospital>> getAll(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(hospitalService.getAll());
    }
}
