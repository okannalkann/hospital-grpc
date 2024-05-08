package com.hospitalservice.service.grpc;

import com.oalkan.grpc.PatientRequest;
import com.oalkan.grpc.PatientResponse;

public interface PatientGrpcService {

    PatientResponse getPatient(PatientRequest patientRequest);
}
