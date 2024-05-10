package com.oalkan.patientservice.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class HospitalResponseDTO {
    private int hospitalId;
    private String name;
    private String address;
    private String phone;
    private int capacity;

    public HospitalResponseDTO(int hospitalId, String name, String address, String phone, int capacity){
        this.hospitalId = hospitalId;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.capacity = capacity;
    }
}

