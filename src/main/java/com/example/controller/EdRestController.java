package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.binding.EligResponse;
import com.example.service.EligService;

@RestController
public class EdRestController {
    
	@Autowired
	private EligService  service;
	
	@GetMapping("/eligibility/{caseNum}")
	public EligResponse determieEligibility(@PathVariable Long caseNum) {
		EligResponse eligResponse = service.determineEligibility(caseNum);
		
		return eligResponse;
		
	}
}
