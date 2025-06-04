package com.backend.harsh.controller;

import com.backend.harsh.dto.OpdDTO;
import com.backend.harsh.entities.Admin;
import com.backend.harsh.entities.Opd;
import com.backend.harsh.entities.Patient;
import com.backend.harsh.service.AdminService;
import com.backend.harsh.service.OpdService;
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
@RequestMapping("/opd")
@CrossOrigin(
	    origins = "http://localhost:5173",
	    allowedHeaders = "*",
	    allowCredentials = "true"
	)
public class OpdController {

    @Autowired
    private OpdService opdService;
	  @Autowired
	    private HttpSession session;
    @Autowired
    private AdminService adminService;

    @Autowired
    private PatientService patientService;

    @PostMapping("/add")
    public ResponseEntity<String> addOpd(@RequestBody OpdDTO opdDTO) {
    	  try {Opd opd = mapToEntity(opdDTO);
		    	Long adminId = (Long)  session.getAttribute("adminId");
		         if (adminId == null ) {
		             return ResponseEntity.badRequest().body("Admin not found"+adminId);
		         }
		         Admin admin = adminService.getAdminById(adminId);
		         if (admin == null) {
		             return ResponseEntity.badRequest().body("Admin not found");
		         }
		        Patient patient = patientService.getPatientById(opdDTO.getCasePaperId());
		        if (patient == null) {
		            return ResponseEntity.badRequest().body("Patient not found");
		        }
		
		        // Map DTO to Entity
		        
		        opd.setAdmin(admin);
		        opd.setPatient(patient);
		
		        opdService.addOpd(opd);
		
		        return ResponseEntity.ok("OPD record added successfully");
    	  }
    	  catch (Exception e) {
              return ResponseEntity.status(500)
                      .body("An error occurred while adding the item.\nItem may be already exists");
          }
    }

    @GetMapping("/{id}")
    public ResponseEntity<OpdDTO> getOpdById(@PathVariable Long id) {
        Opd opd = opdService.getOpdById(id);
        if (opd != null) {
            OpdDTO opdDTO = mapToDTO(opd);
            return ResponseEntity.ok(opdDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateOpd(@PathVariable Long id, @RequestBody OpdDTO opdDTO) {
        Opd existingOpd = opdService.getOpdById(id);
        if (existingOpd == null) {
            return ResponseEntity.notFound().build();
        }

        Admin admin = null;
        if (opdDTO.getAdminId() != null) {
            admin = adminService.getAdminById(opdDTO.getAdminId());
            if (admin == null) {
                return ResponseEntity.badRequest().body("Admin not found");
            }
            existingOpd.setAdmin(admin);
        }

        Patient patient = null;
        if (opdDTO.getCasePaperId() != null) {
            patient = patientService.getPatientById(opdDTO.getCasePaperId());
            if (patient == null) {
                return ResponseEntity.badRequest().body("Patient not found");
            }
            existingOpd.setPatient(patient);
        }

        existingOpd.setOpdDate(opdDTO.getOpdDate());
       // existingOpd.setCreateDate(opdDTO.getCreateDate());
        existingOpd.setTotalAmount(opdDTO.getTotalAmount());
        existingOpd.setNotes(opdDTO.getNotes());

        opdService.updateOpd(id, existingOpd);

        return ResponseEntity.ok("OPD record updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteOpd(@PathVariable Long id) {
        opdService.deleteOpd(id);
        return ResponseEntity.ok("OPD record deleted successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<List<OpdDTO>> getAllOpdRecords() {
    	
    	Long adminId = (Long) session.getAttribute("adminId");
        if (adminId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Admin admin = adminService.getAdminById(adminId);
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        List<Opd> opdRecords = opdService.getAllOpdRecords();
        // Map List<Opd> to List<OpdDTO>
        List<OpdDTO> opdDTOs = opdRecords.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(opdDTOs);
    }

    // Helper Method: Convert OpdDTO to Opd Entity
    private Opd mapToEntity(OpdDTO dto) {
        Opd opd = new Opd();
        opd.setId(dto.getOpdId());
        opd.setOpdDate(dto.getOpdDate());
        opd.setCreateDate(LocalDateTime.now());
        opd.setTotalAmount(dto.getTotalAmount());
        opd.setNotes(dto.getNotes());
        return opd;
    }

    // Helper Method: Convert Opd Entity to OpdDTO
    private OpdDTO mapToDTO(Opd opd) {
        OpdDTO dto = new OpdDTO();
        dto.setOpdId(opd.getId());
        dto.setCasePaperId(opd.getPatient().getId());
        dto.setAdminId(opd.getAdmin().getId());
        dto.setOpdDate(opd.getOpdDate());
        dto.setCreateDate(opd.getCreateDate());
        dto.setTotalAmount(opd.getTotalAmount());
        dto.setNotes(opd.getNotes());
        return dto;
    }
}