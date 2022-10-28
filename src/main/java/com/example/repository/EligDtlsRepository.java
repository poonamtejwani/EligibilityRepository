package com.example.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.EligDtlsEntity;

public interface EligDtlsRepository extends JpaRepository<EligDtlsEntity, Serializable>{

}
