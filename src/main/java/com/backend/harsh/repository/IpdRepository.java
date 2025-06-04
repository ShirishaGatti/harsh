package com.backend.harsh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.harsh.entities.Ipd;

public interface IpdRepository extends JpaRepository<Ipd, Long> {
}
