package com.oalkan.patientservice.service;

import com.oalkan.grpc.PatientRequest;
import com.oalkan.grpc.PatientResponse;
import com.oalkan.grpc.PatientServiceGrpc;
import com.oalkan.patientservice.repository.HospitalRepository;
import com.oalkan.patientservice.repository.PatientRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class PatientGrpcServiceImpl extends PatientServiceGrpc.PatientServiceImplBase {

    private final PatientRepository patientRepository;
    private final HospitalRepository hospitalRepository;

    @Override
    public void getPatient(PatientRequest request, StreamObserver<PatientResponse> responseObserver) {

        patientRepository.findByEmail(request.getPatient().getEmail())
                .orElseThrow( () -> new RuntimeException("Patient not found with Email: " + request.getPatient().getEmail()));

        hospitalRepository.findByHospitalId((int)request.getPatient().)
                .orElseThrow( () -> new RuntimeException("Hospital not found with Id");

        responseObserver.onNext(null);
        responseObserver.onCompleted();
        super.getPatient(request, responseObserver);
    }
}
