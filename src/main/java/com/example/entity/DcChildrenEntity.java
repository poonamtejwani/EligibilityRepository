package com.example.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "DC_CHILDREN")
public class DcChildrenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer childId;

    private String childName;

    private Integer childAge;

    private Long childSsn;

    private Long caseNum;
}
