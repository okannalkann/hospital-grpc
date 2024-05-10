package com.oalkan.patientservice.model.dto;

import com.oalkan.patientservice.model.HospitalPatient;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

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

