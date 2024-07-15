package com.dexter.gcv_life.Controller;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dexter.gcv_life.Entity.AuditTracker;
import com.dexter.gcv_life.Entity.FieldMaster;
import com.dexter.gcv_life.Entity.LoginTable;
import com.dexter.gcv_life.Entity.NewUsersDetails;
import com.dexter.gcv_life.Repository.AuditTrackerRepository;
import com.dexter.gcv_life.Repository.FieldMasterRepo;
import com.dexter.gcv_life.Repository.GeographyMasterRepository;
import com.dexter.gcv_life.Repository.LogInRepository;
import com.dexter.gcv_life.Repository.NewUserDetailsRepository;

@RestController
@CrossOrigin
public class BulkDataUploadController {
    @Autowired
    FieldMasterRepo fieldMasterRepository;

    @Autowired
    GeographyMasterRepository geographyMasterRepo;

//    @Autowired
//    FieldMaster fieldMasterEntity;
//
//    @Autowired
//    NewUsersDetails newUsersDetailsObj;

    @Autowired
    NewUserDetailsRepository newUserDetailsRepo;

    @Autowired
    LogInRepository logInRepo;

    @Autowired
    AuditTrackerRepository auditTrackerRepo;


    @GetMapping("/getBulkDataUpload")
    public String bulkDataUpload(){
        try {
            Long currentLastRowId = fieldMasterRepository.getLastRowIdFieldMaster();
            Long lastRowId = auditTrackerRepo.getLastRowIdAuditMaster();

            if(lastRowId.equals(currentLastRowId) && currentLastRowId == (long) 0){
                lastRowId = 0L;
            }
            for (Long i = (lastRowId+1) ; i <= currentLastRowId; i++) {
            	List<FieldMaster> getFieldData = fieldMasterRepository.getEmployeeData(i.intValue());
            	
            	String employeeName = getFieldData.get(0).getEmployee_name();
                String employeeId = getFieldData.get(0).getLogin_id();
                String division = getFieldData.get(0).getDivision();
                String state_name = getFieldData.get(0).getState();
                String zone = geographyMasterRepo.getZoneNameOnStateBasis(state_name);
                String contact = getFieldData.get(0).getContact_no1();
                String email = getFieldData.get(0).getOfficial_email_id();
                String designation = getFieldData.get(0).getDesignation();                
                String password = "pass@123";

                // Split the employeeName into firstName and lastName
                String[] nameParts = employeeName.split(" ");
                String firstName = nameParts[0];
                String lastName = nameParts[nameParts.length - 1];

                // Construct the first name by joining the parts (excluding the last one)
                StringBuilder firstPartBuilder = new StringBuilder();
                for (int k = 1; k < nameParts.length - 1; k++) {
                    firstPartBuilder.append(nameParts[k]);
                    if (k < nameParts.length - 2) {
                        firstPartBuilder.append(" ");
                    }
                }
                firstName = firstName + " " + firstPartBuilder.toString();

                BigInteger checkExistingUser = newUserDetailsRepo.getByUserName(employeeId);
               if(checkExistingUser.intValue() == 0) {
                List<NewUsersDetails> dataNewUser = (List<NewUsersDetails>) newUserDetailsRepo.findByUserId(i);
                
                    if (dataNewUser != null) {
                        NewUsersDetails existingData = dataNewUser.get(0);
                        existingData.setFirstName(firstName);
                        existingData.setLastName(lastName);
                        existingData.setUsername(employeeId);
                        existingData.setDivision(division);
                        existingData.setZone(zone);
                        existingData.setMobileNo(contact);
                        existingData.setEmailId(email);
//                    existingData.setPassword(password);

                        newUserDetailsRepo.save(existingData);

                    } else {
                        NewUsersDetails newData = new NewUsersDetails();
                        newData.setFirstName(firstName);
                        newData.setLastName(lastName);
                        newData.setUsername(employeeId.isEmpty() ? designation+"_"+i : employeeId);
                        newData.setDivision(division);
                        newData.setZone(zone);
                        newData.setMobileNo(contact);
                        newData.setEmailId(email);
                        newData.setPassword(password);
                        newData.setIsactive(1);
                        newData.setRoleIdFk(getRoleId(designation));
                        
                        newUserDetailsRepo.save(newData);
                    }
               
                List<LoginTable> dataLogIn = logInRepo.findByUserId(Math.toIntExact(i));
               
                    if (!dataLogIn.isEmpty()) {
                        LoginTable existingData = dataLogIn.get(0);
                        existingData.setUsername(employeeId);
//                    existingData.setPassword(password);
                        existingData.setUserIdFk((long) i);

                        logInRepo.save(existingData);

                    } else {
                        LoginTable newData = new LoginTable();
                        newData.setUsername(employeeId.isEmpty() ? designation+"_"+i : employeeId);
                        newData.setPassword(password);
                        newData.setUserIdFk((long) i);
                        newData.setIsActive(1);
                        newData.setFlag(1);
                        newData.setRoleIdFk(getRoleId(designation));
                        
                        logInRepo.save(newData);
                    }
               }

                AuditTracker auditTrackerObj = auditTrackerRepo.getById(1);

                auditTrackerObj.setLastRowId(i);
                auditTrackerObj.setEmployeeId(employeeId.isEmpty() ? designation+"_"+i : employeeId);
                
                auditTrackerRepo.save(auditTrackerObj);
                
                System.out.println("Current record Insertion ---->  " + i);
            }

            return "Account Created Successfully";
        } catch (Exception e){
            e.printStackTrace();
            return "Some error occurred";
        }
    }


	private Long getRoleId(String designation) {

		if(designation.equals("Admin")) {
			return (long) 1;
		}
		if(designation.equals("VP") || designation.equals("SR.VP")) {
			return (long) 2;
		}
		if(designation.equals("BH")) {
			return (long) 3;
		}
		if(designation.equals("GM") || designation.equals("Sr GENERAL MANAGER") || designation.equals("SR.GM") || designation.equals("DH") || designation.equals("DGM") || designation.equals("HSM")) {
			return (long) 4;
		}
		if(designation.equals("ZBM") || designation.equals("ZSM") || designation.equals("SR ZSM") || designation.equals("SR ZBM") || designation.equals("SR ZSM") || designation.equals("SM") || designation.equals("ZONAL HEAD")) {
			return (long) 5;
		}
		if(designation.equals("RBM") || designation.equals("RSM") || designation.equals("RKAM")) {
			return (long) 6;
		}
		if(designation.equals("ABM") || designation.equals("ASM") || designation.equals("ASE") || designation.equals("ABE") || designation.equals("SR ABM") || 
		   designation.equals("PABM") || designation.equals("DBM") || designation.equals("DSM") || designation.equals("SR DSM") || designation.equals("PDSM") || 
		   designation.equals("KAM") || designation.equals("PKAM") || designation.equals("PMT") || designation.equals("IBM") || designation.equals("Sr.IBM")) {
			return (long) 7;
		}
		if(designation.equals("BE") || designation.equals("TBM") || designation.equals("FSO") || designation.equals("PABM") || designation.equals("PFSO") || 
				designation.equals("MT") || designation.equals("EXECUTIVE TRAINEE") || designation.equals("PASE") || designation.equals("TM") || designation.equals("PABE") || 
				designation.equals("HBE") || designation.equals("MANAGEMENT TRAINEE") || designation.equals("ECOMMERCE MANAGER") || designation.equals("PKAM") || designation.equals("KAE") || 
				designation.equals("TSM") || designation.equals("SR TSM") || designation.equals("PKAE") || designation.equals("IBM") || designation.equals("PCCS") || 
				designation.equals("CCS") || designation.equals("SR CCS")) {
			return (long) 8;
		}
		return null;
	}

}
