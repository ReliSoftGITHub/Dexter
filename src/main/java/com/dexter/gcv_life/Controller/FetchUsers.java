package com.dexter.gcv_life.Controller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

//import javax.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dexter.gcv_life.Entity.BlockUserRequest;
import com.dexter.gcv_life.Entity.LoginTable;
import com.dexter.gcv_life.Entity.NewUsersDetails;
import com.dexter.gcv_life.Repository.LogInRepository;
import com.dexter.gcv_life.Repository.NewUserDetailsRepository;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@RestController
@CrossOrigin
public class FetchUsers {

	@Autowired
	private NewUserDetailsRepository newUserDetailsRepository;

	@Autowired
	private LogInRepository logInRepo;

	@PostMapping(value = "/fetchUsers")
	public String isAdmin(final HttpServletRequest request, @RequestParam("role") String role,
						  @RequestParam("userrole") String userrole,
						  @RequestParam("employeeid") String employeeid) throws JSONException {
		
		if(userrole.isEmpty())
			userrole = null;
		
		if(employeeid.isEmpty())
			employeeid = null;
		
		System.out.println("Fetch users API called");
		if (role.equals("ADMIN")) {
			
			final JsonObject jsonResponse = new JsonObject();
			BigInteger totalCount = newUserDetailsRepository.totalCount(userrole, employeeid);
			
			long recordsCount = Long.parseLong(request.getParameter("iDisplayLength"));
			long startIndex = Long.parseLong(request.getParameter("iDisplayStart"));
			
			List<NewUsersDetails> userdata = newUserDetailsRepository.findAllData(recordsCount, startIndex, userrole, employeeid);
			JsonElement raw_data = new Gson().toJsonTree(userdata);
			jsonResponse.add("aaData", raw_data);
			jsonResponse.addProperty("iTotalRecords", totalCount);
			jsonResponse.addProperty("iTotalDisplayRecords", totalCount);
	        return jsonResponse.toString();

//			List<NewUsersDetails> findAll = newUserDetailsRepository.findAll();
//			System.out.println(findAll.size());
//			return new ResponseEntity<List<NewUsersDetails>>(findAll, HttpStatus.OK);
		} else {
			List list = new ArrayList<String>();
			list.add("Role not found");
			return list.toString();
//			return new ResponseEntity<>(list, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/updateuser")
	public ResponseEntity<String> updateUser(@RequestBody NewUsersDetails newUser, @RequestParam("role") String role) {
		System.out.println("update user API called");
		try {
			List<NewUsersDetails> listOfUsers = newUserDetailsRepository.findAll();
			NewUsersDetails data = newUserDetailsRepository.findByUserId(newUser.getUserId());

			Integer isACTIVE = data.getIsactive();
			if (role.equals("ADMIN")) {
				for (NewUsersDetails list : listOfUsers) {

//					if (!list.getUsername().isEmpty()) {
//						if (list.getUsername().equals(newUser.getUsername())) {
//							if (list.getUserId() != newUser.getUserId()) {
//								return new ResponseEntity<>("User Name Already Exist..", HttpStatus.BAD_GATEWAY);
//							}
//						}
//					}

					/*
					if (!list.getEmailId().isEmpty()) {
						if (list.getEmailId().equals(newUser.getEmailId())) {
							if (list.getUserId() != newUser.getUserId()) {
								return new ResponseEntity<>("Email ID Already Exist..", HttpStatus.BAD_GATEWAY);
							}
						}
					}

					if (!list.getMobileNo().isEmpty()) {
						if (list.getMobileNo().equals(newUser.getMobileNo())) {
							if (list.getUserId() != newUser.getUserId()) {
								return new ResponseEntity<>("Mobile Number Already Exist..", HttpStatus.BAD_GATEWAY);
							}
						}
					}
					*/
				}

				NewUsersDetails user = new NewUsersDetails();

				user.setUserId(newUser.getUserId());
				user.setUsername(newUser.getUsername());
				user.setFirstName(newUser.getFirstName());
				user.setLastName(newUser.getLastName());
				user.setMobileNo(newUser.getMobileNo());
				user.setZone(newUser.getZone());
				user.setAddress1(newUser.getAddress1());
				user.setDivision(newUser.getDivision());
				user.setEmailId(newUser.getEmailId());
				user.setPassword(newUser.getPassword());
				user.setRoleIdFk(newUser.getRoleIdFk());
					user.setIsactive(1);
				

				newUserDetailsRepository.save(user);

				LoginTable updateUserLoginTable = logInRepo.updateUserLoginTable(newUser.getUserId());

				LoginTable loginTable = new LoginTable();

				loginTable.setId(updateUserLoginTable.getId());
				loginTable.setUsername(newUser.getUsername());
				loginTable.setPassword(newUser.getPassword());
				loginTable.setRoleIdFk(newUser.getRoleIdFk());
				loginTable.setIsActive(1);
				loginTable.setFlag(updateUserLoginTable.getFlag());
				loginTable.setUserIdFk(updateUserLoginTable.getUserIdFk());
				logInRepo.save(loginTable);

				return new ResponseEntity<>("User Updated Succesfully..!!", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Please Select ADMIN Role First..!!", HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("Failed...", HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/deleteuser")
	public ResponseEntity<String> deleteExistingUser(@RequestBody BlockUserRequest request) {
		System.out.println("delete user API called");
		Long id = request.getUserId();
		String role = request.getRole();

		if (role.equals("ADMIN")) {
			if (newUserDetailsRepository.existsById(id)) {
				NewUsersDetails findByUserId = newUserDetailsRepository.findByUserId(id);
				System.out.println(id);
				LoginTable findById = logInRepo.updateUserLoginTable(findByUserId.getUserId());
				System.out.println(findById.getUserIdFk());
				Long id2 = findById.getId();

				logInRepo.deleteById(id2);

				newUserDetailsRepository.deleteById(id);
				return new ResponseEntity<>("User Deleted Succesfully..!!", HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("User Id not present", HttpStatus.BAD_REQUEST);
			}
		}
		return new ResponseEntity<>("Please select ADMIN role first", HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/blockuser")
	public ResponseEntity<String> blockUser(@RequestBody BlockUserRequest request) {
		System.out.println("block user API called");
		Long id = request.getUserId();
		String role = request.getRole();

		String response = "";
		if (role.equals("ADMIN")) {
			if (newUserDetailsRepository.existsById(id)) {
				NewUsersDetails user = newUserDetailsRepository.findByUserId(id);
				LoginTable findById = logInRepo.updateUserLoginTable(user.getUserId());
				if (user.getIsactive() == 1 && findById.getIsActive() == 1) {
					findById.setIsActive(0);
					user.setIsactive(0);
					response = "User Block Successfully";
				} else if (user.getIsactive() == 0 && findById.getIsActive() == 0) {
					findById.setIsActive(1);
					user.setIsactive(1);
					response = "User Unblock Successfully";
				}
				newUserDetailsRepository.save(user);
				System.out.println(findById);
				return new ResponseEntity<String>(response, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("User Id not present", HttpStatus.BAD_REQUEST);
			}
		}
		return new ResponseEntity<String>("Please select ADMIN role first", HttpStatus.BAD_REQUEST);
	}

}