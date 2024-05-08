package com.oalkan.patientservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="firstname")
    private String firstname;

    @Column(name="lastname")
    private String lastname;

    @Column(name="dateofbirth")
    private String dateofbirth;

    @Column(name="gender")
    private String gender;

    @Column(name="address")
    private String address;

    @Column(name="phone")
    private String phone;

    @Column(name="email")
    private String email;

    @Column(name="emergencycontact")
    private String emergencycontact;

    @ManyToOne
    @JoinColumn(name="hospital_id")
    @JsonBackReference
    private Hospital hospital;
}
