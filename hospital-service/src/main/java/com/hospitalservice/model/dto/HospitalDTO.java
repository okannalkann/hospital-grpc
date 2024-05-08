package com.hospitalservice.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class HospitalDTO {
    private int id;
    private String name;
    private String address;
    private String phone;
    private int capacity;
}
