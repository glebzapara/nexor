package com.glebzapara.nexor.services;

import com.glebzapara.nexor.models.Admin;
import com.glebzapara.nexor.repositories.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class AdminService {
    AdminRepository adminRepository;
    private PasswordEncoder passwordEncoder;

    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Admin findById(Integer id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
    }

    public List<Admin> findAllAdmins() {
        return adminRepository.findAll();
    }

    public void deleteById(Integer id) {
        adminRepository.deleteById(id);
    }

    public void registerAdmin(Admin admin) throws Exception {
        if (admin.getName() == null || admin.getName().trim().isEmpty()) {
            throw new Exception("Name cannot be null or empty");
        }

        if (admin.getSurname() == null || admin.getSurname().trim().isEmpty()) {
            throw new Exception("Surname cannot be null or empty");
        }

        if (admin.getEmail() == null || admin.getEmail().trim().isEmpty()) {
            throw new Exception("Email cannot be null or empty");
        }

        if (admin.getPassword() == null || admin.getPassword().trim().isEmpty()) {
            throw new Exception("Password cannot be null or empty");
        }

        admin.setPassword(passwordEncoder.encode(admin.getPassword()));

        adminRepository.save(admin);
    }
}
