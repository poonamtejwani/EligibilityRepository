package com.example.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="ELIGIBILITY_DTLS")
public class EligDtlsEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer edTraceId ;
	
	private Long caseNum;
	private String holderName;
	private Long holderSsn;
	private String planNames;
	private String planStatus;
	private LocalDate planStartDate;
	private LocalDate planEndDate;
	private Double benefitAmt;
	private String denialReason;
	
	
	

}
