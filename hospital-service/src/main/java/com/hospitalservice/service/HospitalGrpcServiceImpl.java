package com.hospitalservice.service;

import com.hospitalservice.model.Hospital;
import com.hospitalservice.repository.HospitalRepository;
import healthcare.HospitalResponse;
import healthcare.HospitalRequest;
import healthcare.HospitalResponse2;
import healthcare.HospitalServiceGrpc;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@GrpcService
@RequiredArgsConstructor
public class HospitalGrpcServiceImpl extends HospitalServiceGrpc.HospitalServiceImplBase {

    @Autowired
    private HospitalRepository hospitalRepository;

    @Override
    public void checkHospitalExists(HospitalRequest request, StreamObserver<HospitalResponse> responseObserver) {
        try {
            boolean exists = hospitalRepository.existsById((int) request.getHospitalId());
            HospitalResponse response = HospitalResponse.newBuilder()
                    .setExists(exists)
                    .build();
            responseObserver.onNext(response);
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription("Internal server error: " + e.getMessage()).asRuntimeException());
        } finally {
            responseObserver.onCompleted();
        }
    }

    @Override
    public void getHospital(HospitalRequest request, StreamObserver<HospitalResponse2> responseObserver) {
        try {
            boolean exists = hospitalRepository.existsById((int) request.getHospitalId());
            Optional<Hospital> hospital = hospitalRepository.findById(request.getHospitalId());
            HospitalResponse2 response = HospitalResponse2.newBuilder()
                    .setExists(exists)
                    .setName(hospital.get().getName())
                    .setAddress(hospital.get().getAddress())
                    .setPhone(hospital.get().getPhone())
                    .setCapactiy(hospital.get().getCapacity())
                    .build();
            responseObserver.onNext(response);
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription("Internal server error: " + e.getMessage()).asRuntimeException());
        } finally {
            responseObserver.onCompleted();
        }
    }
}


