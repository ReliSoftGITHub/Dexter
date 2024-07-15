package com.dexter.gcv_life.Controller;

import com.dexter.gcv_life.Entity.ChangePasswordRequest;
import com.dexter.gcv_life.Service.ChangePasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class ChangePasswordController {
    @Autowired
    ChangePasswordService serviceObj;
    @PostMapping("/changePassword")
    public ResponseEntity<String> loginC(@RequestBody ChangePasswordRequest request) throws JSONException {
    	System.out.println("changePassword API called");
        ResponseEntity<String> isValid;
        isValid = serviceObj.changePasswordS(request.getUsername(),request.getOldPassword(),request.getNewPassword());
        return isValid;
    }
}
