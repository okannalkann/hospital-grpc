package com.hospitalservice.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PatientDTO {
    private int id;
    private String firstname;
    private String lastname;
    private String dateofbirth;
    private String gender;
    private String address;
    private String phone;
    private String email;
    private String emergencycontact;
}
