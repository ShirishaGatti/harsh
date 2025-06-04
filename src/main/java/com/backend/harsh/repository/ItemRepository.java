package com.backend.harsh.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.harsh.entities.Item;

public interface ItemRepository extends JpaRepository<Item, Integer> {

	Optional<Item> findByName(String name);
}
