package com.backend.harsh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.harsh.entities.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
