package com.dexter.gcv_life.Controller;

import com.dexter.gcv_life.Repository.LogInRepository;
import com.dexter.gcv_life.Repository.NewUserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class RoleDropDown {
    @Autowired
    private LogInRepository userRepository;

    @Autowired
    NewUserDetailsRepository newUserDetailsRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/getRoleDropDown")
    public Map<Long,String> getRoleDropDown(){
        String roleName = "";
        Map<Long,String> roleNameMap = new HashMap<>();
        long roleId = 0;
        List<Map<String, Object>> roleIdViseRole = jdbcTemplate
                .queryForList("SELECT * FROM role_table");
        for (Map<String, Object> rowRoleIdViseRole : roleIdViseRole){
            roleName = (String) rowRoleIdViseRole.get("roles");
            roleId = (long) rowRoleIdViseRole.get("role_id");
            roleNameMap.put(roleId,roleName);

        }
        return roleNameMap;
    }
}
