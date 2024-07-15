package com.dexter.gcv_life.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dexter.gcv_life.Entity.NewUsersDetails;
import com.dexter.gcv_life.Entity.ViewUseRequest;
import com.dexter.gcv_life.Repository.NewUserDetailsRepository;

@RestController
@CrossOrigin
public class ViewUserController {

	@Autowired
	private NewUserDetailsRepository newUserDetailsRepository;

	@PostMapping("/viewUsername")
	public ResponseEntity<List<NewUsersDetails>> viewUsers(@RequestBody ViewUseRequest request) {
		System.out.println("viewUsername API called");
		Integer role = request.getRole();
		String username = request.getUsername();

		try {

			if (role != null) {

				if (role != null && !username.isEmpty()) {
					List<NewUsersDetails> data = newUserDetailsRepository.findRoleAndUserNames(role, username);
					return new ResponseEntity<List<NewUsersDetails>>(data, HttpStatus.OK);
				}

				if (role != null) {
					List<NewUsersDetails> data = newUserDetailsRepository.findOnlyRole(role);
					return new ResponseEntity<List<NewUsersDetails>>(data, HttpStatus.OK);
				}

			} else {
				List list = new ArrayList();
				list.add("Role is Empty");
				return new ResponseEntity<List<NewUsersDetails>>(list, HttpStatus.BAD_GATEWAY);
			}
		} catch (Exception e) {
			return new ResponseEntity<List<NewUsersDetails>>(HttpStatus.BAD_GATEWAY);
		}
		return new ResponseEntity<List<NewUsersDetails>>(HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/usernameDropDown")
	public ResponseEntity<List> dropDown(){
		System.out.println("usernameDropDown API called");
		List list = newUserDetailsRepository.usernameDistinct();
		return new ResponseEntity<>(list,HttpStatus.OK);
	}
}
