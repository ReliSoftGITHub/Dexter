package com.dexter.gcv_life.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dexter.gcv_life.Entity.LoginTable;
import com.dexter.gcv_life.Repository.LogInRepository;
import com.dexter.gcv_life.Repository.LoginInfo;
import com.dexter.gcv_life.Repository.NewUserDetailsRepository;

@Service
public class ChangePasswordService {
    @Autowired
    private LogInRepository userRepository;
   
    @Autowired
    NewUserDetailsRepository newUserDetailsRepository;

    public ResponseEntity<String> changePasswordS(String username, String oldPassword, String newPassword) throws JSONException {
        String response = "";
        try {
            LoginInfo loginInfo = userRepository.findPasswordAndRoleIdFkByUsername(username);
            Optional<LoginTable> existingData = userRepository.findByUsername(username);
            if (loginInfo != null && loginInfo.getPassword().equals(oldPassword) && loginInfo.getRoleIdFk() != null) {

            	LoginTable findUser = userRepository.findUser(username);
            	System.out.println(findUser);
            	newUserDetailsRepository.updatePassword(newPassword, findUser.getUserIdFk());
            	
                LoginTable loginTableEntityObj = existingData.get();
                loginTableEntityObj.setPassword(newPassword);
                loginTableEntityObj.setFlag(1);
                userRepository.save(loginTableEntityObj);
                response = "true";
                // Set appropriate HTTP status code and headers
                return ResponseEntity.status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response);
            } else {
                response = "false";
                return ResponseEntity.status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();

            // Handle any exception, you can customize the error response as needed
            response = "An error occurred during login";

            // Set appropriate HTTP status code and headers
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        }
    }
}
