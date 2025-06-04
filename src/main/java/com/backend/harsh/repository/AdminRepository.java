package com.backend.harsh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.harsh.entities.Admin;
public interface AdminRepository extends JpaRepository<Admin, Long> {

    Admin findByUserId(String userId);

}
