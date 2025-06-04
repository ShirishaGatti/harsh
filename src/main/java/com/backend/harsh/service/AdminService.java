package com.backend.harsh.service;

import com.backend.harsh.entities.Admin;
import java.util.List;

public interface AdminService {
    Admin addAdmin(Admin admin);
    Admin loginAdmin(Long id,Admin admin);
    Admin getAdminById(Long id);
    Admin updateAdmin(Long id, Admin admin);
    boolean deleteAdmin(Long id);
    List<Admin> getAllAdmins();
	//Admin getAdminByUserId(Admin admin);
	Admin authenticateAdmin(String userId, String password);
}
