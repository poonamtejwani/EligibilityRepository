package com.example.service;

import com.example.binding.EligResponse;

public interface EligService {

	public EligResponse determineEligibility(Long caseNum);
}
