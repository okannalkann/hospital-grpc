package com.hospitalservice.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class HospitalDTO {
    private int id;
    private String name;
    private String address;
    private String phone;
    private int capacity;
    private List<PatientDTO> patients;
}
