package com.backend.harsh.controller;

import com.backend.harsh.dto.IpdDTO;
import com.backend.harsh.entities.Admin;
import com.backend.harsh.entities.Ipd;
import com.backend.harsh.entities.Patient;
import com.backend.harsh.service.AdminService;
import com.backend.harsh.service.IpdService;
import com.backend.harsh.service.PatientService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ipd")
@CrossOrigin(
    origins = "http://localhost:5173",
    allowedHeaders = "*",
    allowCredentials = "true"
)
public class IpdController {

    @Autowired
    private IpdService ipdService;
    @Autowired
    private HttpSession session;
    @Autowired
    private AdminService adminService;
    @Autowired
    private PatientService patientService;

    @PostMapping("/add")
    public ResponseEntity<String> addIpd(@RequestBody IpdDTO ipdDTO) {
        Long adminId = (Long) session.getAttribute("adminId");
        if (adminId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Admin not logged in");
        }
        Admin admin = adminService.getAdminById(adminId);
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Admin not found");
        }

        Patient patient = patientService.getPatientById(ipdDTO.getCasePaperId());
        if (patient == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Patient not found");
        }

        Ipd ipd = mapToEntity(ipdDTO);
        ipd.setAdmin(admin);
        ipd.setPatient(patient);
        ipdService.addIpd(ipd);

        return ResponseEntity.ok("IPD record added successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<IpdDTO> getIpdById(@PathVariable Long id) {
        Long adminId = (Long) session.getAttribute("adminId");
        if (adminId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Ipd ipd = ipdService.getIpdById(id);
        if (ipd != null) {
            IpdDTO ipdDTO = mapToDTO(ipd);
            return ResponseEntity.ok(ipdDTO);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateIpd(@PathVariable Long id, @RequestBody IpdDTO ipdDTO) {
        Long adminId = (Long) session.getAttribute("adminId");
        if (adminId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Admin not logged in");
        }
        Ipd existingIpd = ipdService.getIpdById(id);
        if (existingIpd == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Admin admin = null;
        if (ipdDTO.getAdminId() != null) {
            admin = adminService.getAdminById(ipdDTO.getAdminId());
            if (admin == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Admin not found");
            }
            existingIpd.setAdmin(admin);
        }

        Patient patient = null;
        if (ipdDTO.getCasePaperId() != null) {
            patient = patientService.getPatientById(ipdDTO.getCasePaperId());
            if (patient == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Patient not found");
            }
            existingIpd.setPatient(patient);
        }

        existingIpd.setAdmissionDate(ipdDTO.getAdmissionDate());
        existingIpd.setDischargeDate(ipdDTO.getDischargeDate());
        existingIpd.setNotes(ipdDTO.getNotes());

        ipdService.updateIpd(id, existingIpd);

        return ResponseEntity.ok("IPD record updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteIpd(@PathVariable Long id) {
        Long adminId = (Long) session.getAttribute("adminId");
        if (adminId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Admin not logged in");
        }
        ipdService.deleteIpd(id);
        return ResponseEntity.ok("IPD record deleted successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<List<IpdDTO>> getAllIpdRecords() {
        Long adminId = (Long) session.getAttribute("adminId");
        if (adminId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Admin admin = adminService.getAdminById(adminId);
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        List<Ipd> ipdRecords = ipdService.getAllIpdRecords();
        List<IpdDTO> ipdDTOs = ipdRecords.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ipdDTOs);
    }

    private Ipd mapToEntity(IpdDTO dto) {
        Ipd ipd = new Ipd();
        ipd.setId(dto.getIpdId());
        ipd.setAdmissionDate(dto.getAdmissionDate());
        ipd.setDischargeDate(dto.getDischargeDate());
        ipd.setNotes(dto.getNotes());
        ipd.setCreateDate(LocalDateTime.now());
        return ipd;
    }


    private IpdDTO mapToDTO(Ipd ipd) {
        IpdDTO dto = new IpdDTO();
        dto.setIpdId(ipd.getId());
        dto.setCasePaperId(ipd.getPatient().getId());
        dto.setAdminId(ipd.getAdmin().getId());
        dto.setAdmissionDate(ipd.getAdmissionDate());
        dto.setDischargeDate(ipd.getDischargeDate());
        dto.setNotes(ipd.getNotes());
        dto.setCreateDate(ipd.getCreateDate());
        // Calculate total amount from ConsumedItem's SelectedItems
        double totalAmount = ipd.getConsumedItems().stream()
                .flatMap(ci -> ci.getSelectedItems().stream())
                .mapToDouble(si -> (si.getQuantity() != null && si.getPrice() != null) 
                    ? si.getQuantity() * si.getPrice() : 0.0)
                .sum();
        dto.setAmount(totalAmount);
        return dto;
    }
}