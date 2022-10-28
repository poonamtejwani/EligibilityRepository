package com.example.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.repository.CitizenAppRepository;
import com.example.repository.CoTriggerRepository;
import com.example.repository.DcCaseRepo;
import com.example.repository.DcChildrenRepo;
import com.example.repository.DcEducationRepo;
import com.example.repository.DcIncomeRepo;
import com.example.repository.EligDtlsRepository;
import com.example.repository.PlanRepository;
import com.example.binding.EligResponse;
import com.example.entity.CitizenAppEntity;
import com.example.entity.CoTriggerEntity;
import com.example.entity.DcCaseEntity;
import com.example.entity.DcChildrenEntity;
import com.example.entity.DcEducationEntity;
import com.example.entity.DcIncomeEntity;
import com.example.entity.EligDtlsEntity;
import com.example.entity.PlanEntity;

@Service
public class EligServiceImpl implements EligService {
	
	@Autowired
	private DcCaseRepo dccaseRepo;
	
	@Autowired
	private DcIncomeRepo incomeRepo;
	
	@Autowired
	private PlanRepository planRepo;
	
	@Autowired
	private DcChildrenRepo childRepo;
	
	@Autowired
	private CitizenAppRepository appRepo;
	
	@Autowired
	private DcEducationRepo eduRepo;
	
	@Autowired
	private EligDtlsRepository eligRepo;
	
	@Autowired
	private CoTriggerRepository coTrgRepo;
	
	public  EligResponse determineEligibility(Long caseNum) {
		
	Optional<DcCaseEntity> caseEntity = dccaseRepo.findById(caseNum);
	Integer planId = null;
	String planName= null;
	Integer appId= null;
		
	if(caseEntity.isPresent())
    {
		DcCaseEntity dcCaseEntity = caseEntity.get();
		planId= dcCaseEntity.getPlanId();
		appId = dcCaseEntity.getAppId();
	}
		
	Optional<PlanEntity> planEntity = planRepo.findById(planId);
	if(planEntity.isPresent())
	{
		PlanEntity plan = planEntity.get();
		planName= plan.getPlanName();
	}
	
	Optional<CitizenAppEntity> app = appRepo.findById(planId);
	Integer age=0;
	CitizenAppEntity citizenAppEntity= null;
	if(app.isPresent()) 
	{
		citizenAppEntity = app.get();
		LocalDate dob = citizenAppEntity.getDob();
		LocalDate now = LocalDate.now();
		age=Period.between(dob, now).getYears();
	}
	
	EligResponse eligResponse= executePlanConditions(caseNum , planName, appId);
	
			EligDtlsEntity eligEntity = new EligDtlsEntity();
	        BeanUtils.copyProperties(eligResponse, eligEntity);
	        
	        eligEntity.setCaseNum(caseNum);
	        eligEntity.setHolderName(citizenAppEntity.getFullName());
	        eligEntity.setHolderSsn(citizenAppEntity.getSsn());
	        
	        eligRepo.save(eligEntity);
	        
	        CoTriggerEntity  coEntity= new CoTriggerEntity();
	        coEntity.setCaseNum(caseNum);
		     coEntity.setTrgStatus("Pending");
		     coTrgRepo.save(coEntity);
		     
		 return eligResponse;
		
	}
  private EligResponse executePlanConditions(Long caseNum ,String planName , Integer appId) {
	  EligResponse response = new EligResponse();
	  response.setPlanNames(planName);
	  
	  DcIncomeEntity income = incomeRepo.findByCaseNum(caseNum);
	  
	  if("SNAP".equals(planName))
	  {
	  	
	  	Double empIncome= income.getEmpIncome();
	  	if(empIncome <= 300)
	  	{
	  		response.setPlanStatus("Approved");
	  	}else {
	  		response.setPlanStatus("Deny");
	  		response.setDenialReason("High Income");
	  	}
	  }
	  else if("CCAP".equals(planName)) {
		  
		  boolean  ageCondition= false;
		  boolean kidsCountCondition =  false;
		  
		  List<DcChildrenEntity> childs= childRepo.findByCaseNum(caseNum);
		  
		  if(!childs.isEmpty()) {
			  kidsCountCondition = true;
			  
			for(DcChildrenEntity entity : childs) {
				Integer childAge= entity.getChildAge();
				if(childAge > 16) {
					ageCondition= false;
					break;
				}
			}
		  }
		  if(income.getEmpIncome() <= 300 && kidsCountCondition && ageCondition) 
		  {
			  response.setPlanStatus("Approved");
		  }else
		  {
			   response.setPlanStatus("Deny");
			   response.setDenialReason("Not satisfied Bussiness Rule");
		  }
	     }else if("Medicare".equals(planName)) {
		  
		  Double empIncome = income.getEmpIncome();
		  Double propertyIncome = income.getPropertyIncome();
		  
		  if(empIncome <= 300 && propertyIncome == 0) {
			  response.setPlanStatus("Approved");
		  }else 
		  {
			  response.setPlanStatus("Deny");
			  response.setDenialReason("High Income");
		  }
		  
	     }else if("Medicaid".equals(planName))
	     {
		  Optional<CitizenAppEntity> app = appRepo.findById(appId);
		  if(app.isPresent())
		  {
			  CitizenAppEntity citizenAppEntity = app.get();
			  LocalDate dob = citizenAppEntity.getDob();
			  LocalDate now= LocalDate.now();
			  int age=Period.between(dob, now).getYears();
			  if(age >= 65)
			  {
				  response.setPlanStatus("Approved");
			  }else {
				  response.setPlanStatus("Deny");
				  response.setDenialReason("Age not Matched");
			  }
		  }
		  
	    }else if("NJW".equals(planName))
	    {
		DcEducationEntity educationEntity= eduRepo.findByCaseNum(caseNum); 
        Integer graduationYear = educationEntity.getGraduationYear();
        int currYear=LocalDate.now().getYear();
        
        if(income.getEmpIncome() <=  0 && graduationYear < currYear) {
        	response.setPlanStatus("Approved");
        }else 
        {
        	response.setPlanStatus("Deny");
        	response.setDenialReason("Rules not Satisfied");
        }
	  }
	  
	  if(response.getPlanStatus().equals("Approved"))
	  {
		  response.setPlanStartDate(LocalDate.now());
		  response.setPlanEndDate(LocalDate.now().plusMonths(6));
		  response.setBenefitAmt(350.00);
	  }
	  return response;
  }
   
}
