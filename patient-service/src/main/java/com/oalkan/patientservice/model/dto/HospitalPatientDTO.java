package com.oalkan.patientservice.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter
@Setter
public class HospitalPatientDTO {
    private int id;
    private int patient;
    private int hospital;
    private Date dateRegistered;
    private Date dateDischarged;
}

