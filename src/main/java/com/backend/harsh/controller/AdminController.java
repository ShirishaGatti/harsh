package com.backend.harsh.controller;

import com.backend.harsh.dto.AdminDTO;
import com.backend.harsh.exceptions.ResourceNotFoundException;
import com.backend.harsh.dto.IpdDTO;
import com.backend.harsh.entities.Admin;
import com.backend.harsh.entities.Ipd;
import com.backend.harsh.entities.Patient;
import com.backend.harsh.service.AdminService;

import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

@RestController
@RequestMapping("/admin")
@CrossOrigin(
	    origins = "http://localhost:5173",
	    allowedHeaders = "*",
	    allowCredentials = "true"
	)
public class AdminController {

	  @Autowired
	    private HttpSession session;
	  
    @Autowired
    private AdminService adminService;
    
//    @Autowired
//    private SessionStatus sessionStatus;

    @PostMapping("/add")
    public ResponseEntity<String> addAdmin(@RequestBody AdminDTO adminDTO) {
       
        // Map DTO to Entity
       try {
        Admin admin=mapToEntity(adminDTO);
        if (admin.getStatus()==null) {
            admin.setStatus("Default status"); 
        }
        if(admin.getName().equals("") && admin.getUserId().equals("") && admin.getPassword().equals("")) {
            return ResponseEntity.status(500).body("All Fields are required");
        }

        //admin.setStatus(null);
       //admin.setUserId(adminDT private Admin mapToEntity(AdminDTO adminDTO) {
		// TODO Auto-generated method stub
        adminService.addAdmin(admin);

        return ResponseEntity.ok("Admin added successfully");
       }
       
       catch (Exception ex) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred while processing ");
       }
		//return null;
	
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AdminDTO adminDTO, HttpSession session) {
        try {
            // Authenticate the admin based on userId and password
            Admin admin = adminService.authenticateAdmin(adminDTO.getUserId(), adminDTO.getPassword());

            if (admin != null ) {
             //   session.setAttribute("status", admin.setStatus("Active"));
                session.setAttribute("adminId", admin.getId());
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Login successful");
                response.put("adminId", admin.getId());
                response.put("name", admin.getName());

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred while processing the login request");
        }
    }
        // Map DTO to Entity
    @GetMapping("/{id}")
    public ResponseEntity<?> getAdminById(@PathVariable Long id) {
        try {
            Admin admin = adminService.getAdminById(id);
            if (admin != null) {
                return ResponseEntity.ok(admin);
            } else {
                throw new ResourceNotFoundException("Admin not found with ID: " + id);
            }
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("An unexpected error occurred while retrieving the admin");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAdmin(@PathVariable Long id, @RequestBody Admin admin) {
        try {
            Admin updatedAdmin = adminService.updateAdmin(id, admin);
            if (updatedAdmin != null) {
                return ResponseEntity.ok("Admin updated successfully");
            } else {
                throw new ResourceNotFoundException("Admin not found with ID: " + id);
            }
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("An unexpected error occurred while updating the admin");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteAdmin(@PathVariable Long id) {
        try {
            boolean isDeleted = adminService.deleteAdmin(id);
            if (isDeleted) {
                return ResponseEntity.ok("Admin deleted successfully");
            } else {
                throw new ResourceNotFoundException("Admin not found with ID: " + id);
            }
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("An unexpected error occurred while deleting the admin");
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session, SessionStatus sessionStatus) {
        session.invalidate();              // Invalidate HTTP session
        sessionStatus.setComplete();       // Clear @SessionAttributes if any
        return ResponseEntity.ok("Logged out successfully");
    }

//    @PostMapping("/logout")
//    public ResponseEntity<String> logout(HttpSession session) {
//        session.invalidate();
//        sessionStatus.setComplete(); // Important
//        System.out.println("Session invalidated. ID: " + session.getId());
//
//        return ResponseEntity.ok("Logged out successfully");
//    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllAdmins() {
        try {
            List<Admin> admins = adminService.getAllAdmins();
            if (admins != null && !admins.isEmpty()) {
                return ResponseEntity.ok(admins);
            } else {
                return ResponseEntity.status(404).body("No admins found in the system");
            }
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("An unexpected error occurred while retrieving admins");
        }
    }
    private Admin mapToEntity(AdminDTO dto) {
        
        Admin admin=new Admin();
        admin.setName(dto.getName());
       
        admin.setUserId(dto.getUserId());
        admin.setMobile(dto.getUserId());
        admin.setPassword(dto.getPassword());
        //admin.setStatus(null);
        return admin;
    }

    // Helper Method: Convert Ipd Entity to IpdDTO
    private AdminDTO mapToDTO(Admin admin) {
      
        AdminDTO dto=new AdminDTO();
        dto.setName(admin.getName());
        dto.setUserId(admin.getUserId());
        dto.setMobile(admin.getMobile());
        dto.setPassword(admin.getPassword());
        //login kela ki tithun te fetch karaych ahe 
      //  dto.setStatus(admin.getStatus());
        return dto;
    }

}
