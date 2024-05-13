package com.hospitalservice.controller;

import com.hospitalservice.model.Hospital;
import com.hospitalservice.model.dto.HospitalDTO;
import com.hospitalservice.repository.HospitalRepository;
import com.hospitalservice.service.HospitalService;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class HospitalControllerTest {
    @Mock
    private HospitalRepository hospitalRepository;

    @InjectMocks
    private HospitalService hospitalService;

    @Test
    public void updateHospital_success() {
        // Arrange
        Hospital existingHospital = new Hospital(1, "Test Hospital", "123 Test St", "1234567890", 500);
        HospitalDTO hospitalDTO = HospitalDTO.builder()
                .id(1)
                .name("Updated Hospital")
                .address("456 Updated St")
                .phone("0987654321")
                .capacity(600)
                .patients(null).build();

        when(hospitalRepository.findById(1)).thenReturn(java.util.Optional.of(existingHospital));
        when(hospitalRepository.save(any(Hospital.class))).thenReturn(existingHospital);

        // Act
        Hospital updatedHospital = hospitalService.update(1, hospitalDTO);

        // Assert
        assertNotNull(updatedHospital);
        assertEquals("Updated Hospital", updatedHospital.getName());
        assertEquals("456 Updated St", updatedHospital.getAddress());
        assertEquals("0987654321", updatedHospital.getPhone());
        assertEquals(600, updatedHospital.getCapacity());
    }

    @Test
    public void deleteHospital_success() {
        // Arrange
        when(hospitalRepository.findById(1)).thenReturn(java.util.Optional.of(new Hospital()));
        doNothing().when(hospitalRepository).deleteById(1);

        // Act
        boolean result = hospitalService.delete(1);

        // Assert
        assertTrue(result);
    }

    @Test
    public void addHospital_success() {
        // Arrange
        HospitalDTO hospitalDTO = HospitalDTO.builder()
                .id(1)
                .name("New Hospital")
                .address("789 New St")
                .phone("1928374650")
                .capacity(300)
                .patients(null).build();

        Hospital hospital = new Hospital(1, "New Hospital", "789 New St", "1928374650", 300);

        when(hospitalRepository.save(any(Hospital.class))).thenReturn(hospital);

        // Act
        Hospital savedHospital = hospitalService.add(hospitalDTO);

        // Assert
        assertNotNull(savedHospital);
        assertEquals("New Hospital", savedHospital.getName());
    }
}