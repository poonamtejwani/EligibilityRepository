package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.DcEducationEntity;

import java.io.Serializable;

public interface DcEducationRepo extends JpaRepository<DcEducationEntity, Serializable> {

    DcEducationEntity findByCaseNum(Long caseNum);
}
