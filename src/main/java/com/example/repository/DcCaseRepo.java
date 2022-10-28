package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.DcCaseEntity;

import java.io.Serializable;

public interface DcCaseRepo extends JpaRepository<DcCaseEntity, Serializable> {

    DcCaseEntity findByAppId(Integer appId);
}
