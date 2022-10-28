package com.example.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.CoTriggerEntity;

public interface CoTriggerRepository  extends JpaRepository<CoTriggerEntity, Serializable>{

}
