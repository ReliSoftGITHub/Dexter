package com.dexter.gcv_life.Controller;

import com.dexter.gcv_life.Entity.LoginRequest;
import com.dexter.gcv_life.Service.LogInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class LogInController {
    @Autowired
    LogInService serviceObj;

    @PostMapping("/login")
    public ResponseEntity<String> loginC(@RequestBody LoginRequest request) throws JSONException {
        ResponseEntity<String> isValid;
        isValid = serviceObj.loginS(request.getUsername(),request.getPassword());
        return isValid;
    }

}
