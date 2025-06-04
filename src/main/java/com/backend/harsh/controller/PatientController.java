package com.backend.harsh.controller;

import com.backend.harsh.dto.PatientDTO;
import com.backend.harsh.entities.Admin;
import com.backend.harsh.entities.Patient;
import com.backend.harsh.service.AdminService;
import com.backend.harsh.service.PatientService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/patient")
@CrossOrigin(
	    origins = "http://localhost:5173",
	    allowedHeaders = "*",
	    allowCredentials = "true"
	)
public class PatientController {


	  @Autowired
	    private HttpSession session;
	  
    @Autowired
    private PatientService patientService;

    @Autowired
    private AdminService adminService;

    @PostMapping("/add")
    public ResponseEntity<String> addPatient(@RequestBody PatientDTO patientDTO) {

        
        Patient patient = mapToEntity(patientDTO);
      Object adminId =   session.getAttribute("adminId");
      if (adminId == null ) {
          return ResponseEntity.badRequest().body("Admin not found"+adminId);
      }
      Admin admin = adminService.getAdminById((Long) adminId);
      if (admin == null) {
    	    return ResponseEntity.badRequest().body("Admin not found with ID: " + adminId);
    	}
        patient.setAdmin(admin);
        patientService.addPatient(patient);

        return ResponseEntity.ok("Patient added successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable Long id) {
        Patient patient = patientService.getPatientById(id);
        if (patient != null) {
            PatientDTO patientDTO = mapToDTO(patient);
            return ResponseEntity.ok(patientDTO);
        } 
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updatePatient(@PathVariable Long id, @RequestBody PatientDTO patientDTO) {
        Patient existingPatient = patientService.getPatientById(id);
        if (existingPatient == null) {
            return ResponseEntity.notFound().build();
        }

        Admin admin = null;
        if (patientDTO.getAdminId() != null) {
            admin = adminService.getAdminById(patientDTO.getAdminId());
            if (admin == null) {
                return ResponseEntity.badRequest().body("Admin not found");
            }
            existingPatient.setAdmin(admin);
        }

        // Map updated fields from DTO to entity
        existingPatient.setName(patientDTO.getName());
        existingPatient.setMobile(patientDTO.getMobile());
        existingPatient.setAddress(patientDTO.getAddress());
        existingPatient.setGender(patientDTO.getGender());
        existingPatient.setStatus(patientDTO.getStatus());
        existingPatient.setNotes(patientDTO.getNotes());

        patientService.updatePatient(id, existingPatient);

        return ResponseEntity.ok("Patient updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.ok("Patient deleted successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
    	
    	Long adminId = (Long) session.getAttribute("adminId");
        if (adminId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Admin admin = adminService.getAdminById(adminId);
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        List<Patient> patients = patientService.getAllPatients();

        List<PatientDTO> patientDTOs = patients.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(patientDTOs);
    }

    private Patient mapToEntity(PatientDTO dto) {
        Patient patient = new Patient();
        patient.setName(dto.getName());
        patient.setMobile(dto.getMobile());
        patient.setAddress(dto.getAddress());
        patient.setGender(dto.getGender());
        patient.setStatus(dto.getStatus());
        patient.setNotes(dto.getNotes());
        patient.setRegDate(dto.getRegDate());

        return patient;
    }

    private PatientDTO mapToDTO(Patient patient) {
        PatientDTO dto = new PatientDTO();

        dto.setPatientId(patient.getId());
        dto.setName(patient.getName());
        dto.setMobile(patient.getMobile());
        dto.setAddress(patient.getAddress());
        dto.setGender(patient.getGender());
        dto.setStatus(patient.getStatus());
        dto.setNotes(patient.getNotes());
        dto.setAdminId(patient.getAdmin().getId());
        dto.setRegDate(patient.getRegDate());
        return dto;
    }
}
