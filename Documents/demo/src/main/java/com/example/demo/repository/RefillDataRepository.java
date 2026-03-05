package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.RefillDataEntity;

@Repository
public interface RefillDataRepository extends JpaRepository<RefillDataEntity, Long> {
}