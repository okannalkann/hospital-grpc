package com.oalkan.patientservice.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class HospitalCheckResponseDTO {
    private boolean exist;
}

