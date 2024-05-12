package com.oalkan.patientservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oalkan.patientservice.model.Patient;
import com.oalkan.patientservice.model.dto.PatientDTO;
import com.oalkan.patientservice.service.PatientService;
import healthcare.HospitalResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientController.class)
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @Test
    public void getPatientById_successfully() throws Exception {
        // Arrange
        Patient patient = new Patient();
        patient.setId(1);
        patient.setFirstname("John");
        patient.setLastname("Doe");

        given(patientService.getById(1)).willReturn(patient);

        mockMvc.perform(get("/api/patient/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    public void createPatient_successfully() throws Exception {
        // Prepare test data
        PatientDTO patientDTO = PatientDTO.builder()
                .id(1)
                .firstname("John")
                .lastname("Durden")
                .dateofbirth("27.7.1990")
                .gender("Male")
                .address("Schörstr 24")
                .phone("12341234123")
                .email("john.doe@gmail.com")
                .emergencycontact("1231231234213")
                .build();

        Patient patient = new Patient();
        patient.setId(1);

        given(patientService.findByEmailOrPhone(any(String.class), any(String.class))).willReturn(Optional.empty());
        given(patientService.add(any(PatientDTO.class))).willReturn(patient);

        mockMvc.perform(post("/api/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(patientDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    public void deletePatient_successfully() throws Exception {
        // Arrange
        int patientId = 1;
        when(patientService.delete(patientId)).thenReturn(true);

        mockMvc.perform(delete("/api/patient/{id}", patientId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true));

    }
}
