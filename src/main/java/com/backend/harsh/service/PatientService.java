package com.backend.harsh.service;

import com.backend.harsh.entities.Patient;
import java.util.List;

public interface PatientService {
    Patient addPatient(Patient patient);
    Patient getPatientById(Long id);
    Patient updatePatient(Long id, Patient patient);
    void deletePatient(Long id);
    List<Patient> getAllPatients();
}
