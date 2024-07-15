package com.dexter.gcv_life.Controller;

import com.dexter.gcv_life.Entity.LoginTable;
import com.dexter.gcv_life.Entity.NewUsersDetails;
import com.dexter.gcv_life.Repository.LogInRepository;
import com.dexter.gcv_life.Repository.NewUserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/users")
public class NewUserDetailsController {
    @Autowired
    private NewUserDetailsRepository newUserDetailsRepo;

    @Autowired
    private LogInRepository logInRepo;

    @PostMapping("/register")
    public String registerUser(@RequestBody NewUsersDetails user, @RequestParam(name = "roleIdFk", required = true) Long roleIdFk) {

        // Check if email or mobile number already exist
        if (newUserDetailsRepo.existsByEmailId(user.getEmailId())) {
            return "Email already registered";
        }
        if (newUserDetailsRepo.existsByMobileNo(user.getMobileNo())) {
            return "Mobile number already registered";
        }
        if (newUserDetailsRepo.existsByUsername(user.getUsername())) {
            return "UserName already exists";
        }
        
        user.setRoleIdFk(roleIdFk);
        user.setIsactive(1);
        
        NewUsersDetails newUser = newUserDetailsRepo.save(user);        

        LoginTable loginTableEntityObj = new LoginTable();

        String username = user.getUsername();
        String password = user.getPassword();


        loginTableEntityObj.setUsername(username);
        loginTableEntityObj.setPassword(password);
        loginTableEntityObj.setRoleIdFk(roleIdFk);
        loginTableEntityObj.setIsActive(1);
        loginTableEntityObj.setFlag(0);
        loginTableEntityObj.setUserIdFk(newUser.getUserId());
        logInRepo.save(loginTableEntityObj);
        
        System.out.println(user);
        
        return "User registered successfully";
    }
}
