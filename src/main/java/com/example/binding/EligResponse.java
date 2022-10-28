package com.example.binding;

import java.time.LocalDate;

import lombok.Data;

@Data
public class EligResponse {

	private String planNames;
	private String planStatus;
	private LocalDate planStartDate;
	private LocalDate planEndDate;
	private Double benefitAmt;
	private String denialReason;
}
