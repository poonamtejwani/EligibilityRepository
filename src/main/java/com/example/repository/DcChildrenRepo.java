package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.DcChildrenEntity;

import java.io.Serializable;
import java.util.List;

public interface DcChildrenRepo extends JpaRepository<DcChildrenEntity, Serializable> {

    List<DcChildrenEntity> findByCaseNum(Long caseNum);
}
