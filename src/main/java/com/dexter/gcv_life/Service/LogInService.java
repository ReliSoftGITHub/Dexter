package com.dexter.gcv_life.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.dexter.gcv_life.Entity.LoginTable;
import com.dexter.gcv_life.Entity.NewUsersDetails;
import com.dexter.gcv_life.Repository.LogInRepository;
import com.dexter.gcv_life.Repository.LoginInfo;
import com.dexter.gcv_life.Repository.NewUserDetailsRepository;

import java.util.List;
import java.util.Map;

@Service
public class LogInService {
	@Autowired
	private LogInRepository userRepository;

	@Autowired
	NewUserDetailsRepository newUserDetailsRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ResponseEntity<String> loginS(String username, String password) throws JSONException {
		JSONObject response = new JSONObject();
		try {
			LoginInfo loginInfo = userRepository.findPasswordAndRoleIdFkByUsername(username);
			LoginTable data = null;
			NewUsersDetails findUserName = null;
			
			
			if (newUserDetailsRepository.existsByUsername(username)) {
				data = userRepository.findUser(username);
				findUserName = newUserDetailsRepository.findUserName(username);
			}

			if (data.getIsActive() == 0) {
				return new ResponseEntity<>("User is Blocked.... ", HttpStatus.BAD_GATEWAY);
			}

			if (loginInfo != null && loginInfo.getPassword().equals(password) && loginInfo.getRoleIdFk() != null
					&& data.getIsActive() == 1) {
				
				String firstname = findUserName.getFirstName();
				String lastname = findUserName.getLastName();
				String email = findUserName.getEmailId();
				String address = findUserName.getAddress1();
				String mobileNo = findUserName.getMobileNo();
				String division = findUserName.getDivision();
				String zone = findUserName.getZone();
				String roleName = "";

				Long roleId = loginInfo.getRoleIdFk();
				List<Map<String, Object>> roleIdViseRole = jdbcTemplate
						.queryForList("SELECT * FROM role_table WHERE role_id = \""+roleId+"\"");
				for (Map<String, Object> rowRoleIdViseRole : roleIdViseRole){
					roleName = (String) rowRoleIdViseRole.get("roles");
				}
				
				response.put("status", true);
				response.put("roleName", roleName);
				response.put("roleId", loginInfo.getRoleIdFk());
				response.put("flag", loginInfo.getFlag());
				response.put("firstname", firstname);
				response.put("lastname", lastname);
				response.put("email", email);
				response.put("address", address);
				response.put("mobileNo", mobileNo);
				response.put("division", division);
				response.put("zone", zone);

				// Serialize the JSON response
				String jsonResponse = response.toString();

				// Set appropriate HTTP status code and headers
				return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(jsonResponse);
			} else {
				response.put("status", false);

				// Serialize the JSON response
				String jsonResponse = response.toString();

				// Set appropriate HTTP status code and headers
				return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(jsonResponse);
			}
		} catch (Exception e) {
			e.printStackTrace();

			// Handle any exception, you can customize the error response as needed
			response.put("status", false);
			response.put("error", "An error occurred during login");

			// Serialize the JSON response
			String errorResponse = response.toString();

			// Set appropriate HTTP status code and headers
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
					.body(errorResponse);
		}
	}
}
