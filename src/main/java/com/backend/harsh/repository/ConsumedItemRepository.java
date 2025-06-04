package com.backend.harsh.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.harsh.entities.ConsumedItem;

import java.util.List;

@Repository
public interface ConsumedItemRepository extends JpaRepository<ConsumedItem, Long> {
    List<ConsumedItem> findByIpdId(Long ipdId);
}
