package com.oalkan.patientservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="hospitalPatients")
public class HospitalPatient {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="hospital_id")
    private Integer hospitalId;

    @Column(name = "date_registered")
    private Date dateRegistered;

    @Column(name = "date_discharged", nullable = true)
    private Date dateDischarged;

    @ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @JsonBackReference
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
}
