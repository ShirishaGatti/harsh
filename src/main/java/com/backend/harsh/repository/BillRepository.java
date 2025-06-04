package com.backend.harsh.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.harsh.entities.Bill;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
}
