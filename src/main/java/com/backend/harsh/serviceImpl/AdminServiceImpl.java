package com.backend.harsh.serviceImpl;

import com.backend.harsh.entities.Admin;
import com.backend.harsh.repository.AdminRepository;
import com.backend.harsh.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private final AdminRepository adminRepository;

    
    public AdminServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public Admin addAdmin(Admin admin) {
        return adminRepository.save(admin);
    }
    
    @Override
    public Admin loginAdmin(Long id,Admin admin) {
    	return adminRepository.save(admin);
    }
    @Override
    public Admin getAdminById(Long id) {
        return adminRepository.findById(id).orElse(null);
    }

    @Override
    public Admin updateAdmin(Long id, Admin admin) {
        if (adminRepository.existsById(id)) {
            admin.setId(id);
            return adminRepository.save(admin);
        }
        return null;
    }

    @Override
    public boolean deleteAdmin(Long id) {
        if (adminRepository.existsById(id)) {
            adminRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Admin authenticateAdmin(String userId, String password) {
        Admin admin = adminRepository.findByUserId(userId);
        if (admin != null && admin.getPassword().equals(password)) {
            return admin;
        }
        return null;
    }

}
