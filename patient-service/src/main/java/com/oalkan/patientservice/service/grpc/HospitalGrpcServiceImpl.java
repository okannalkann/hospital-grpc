package com.oalkan.patientservice.service.grpc;

import healthcare.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class HospitalGrpcServiceImpl implements HospitalGrpcService {

    private HospitalServiceGrpc.HospitalServiceBlockingStub hospitalServiceStub;
    private ManagedChannel channel;

    public HospitalGrpcServiceImpl(@Value("${hospital.grpc.host}") String grpcHost, @Value("${hospital.grpc.port}") int grpcPort) {
        System.out.println("--> Hospital grpc: " + grpcHost + " " + grpcPort);
        channel = ManagedChannelBuilder.forAddress(grpcHost, grpcPort)
                .usePlaintext()
                .build();
    }

    @Override
    public HospitalResponse checkHospitalExists(HospitalRequest hospitalId) {
        hospitalServiceStub = HospitalServiceGrpc.newBlockingStub(channel);
        return hospitalServiceStub.checkHospitalExists(hospitalId);
    }

    @Override
    public HospitalResponse2 GetHospital(HospitalRequest hospitalId) {
        hospitalServiceStub = HospitalServiceGrpc.newBlockingStub(channel);
        return hospitalServiceStub.getHospital(hospitalId);
    }
}
