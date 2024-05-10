package com.oalkan.patientservice.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class HospitalResponseDTO {
    private String name;
    private String address;
    private String phone;
    private int capacity;
}

