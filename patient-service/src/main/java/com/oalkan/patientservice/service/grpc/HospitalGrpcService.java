package com.oalkan.patientservice.service.grpc;

import healthcare.HospitalRequest;
import healthcare.HospitalResponse;
import healthcare.HospitalResponse2;

public interface HospitalGrpcService {
    HospitalResponse checkHospitalExists(HospitalRequest hospitalId);
    HospitalResponse2 GetHospital(HospitalRequest hospitalId);
}
