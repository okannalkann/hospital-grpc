package com.hospitalservice.service.grpc;

import com.oalkan.grpc.PatientRequest;
import com.oalkan.grpc.PatientResponse;
import com.oalkan.grpc.PatientServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PatientGrpcServiceImpl implements PatientGrpcService {

    private PatientServiceGrpc.PatientServiceBlockingStub PatientGrpcBlockingStub;
    private ManagedChannel channel;

    public PatientGrpcServiceImpl(@Value("${patient.grpc.host}") String grpcHost, @Value("${patient.grpc.port}") int grpcPort) {
        System.out.println("--> Patient grpc" + grpcHost + ":" + grpcPort);
        channel = ManagedChannelBuilder.forAddress(grpcHost, grpcPort)
                .usePlaintext()
                .build();
    }

    @Override
    public PatientResponse getPatient(PatientRequest patientRequest) {
        PatientGrpcBlockingStub = PatientServiceGrpc.newBlockingStub(channel);
        PatientResponse patientResponse = PatientGrpcBlockingStub.getPatient(patientRequest);
        return patientResponse;
    }
}
