package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.PlanEntity;

import java.io.Serializable;

public interface PlanRepository extends JpaRepository<PlanEntity, Serializable> {

    PlanEntity findByPlanName(String planName);

}
