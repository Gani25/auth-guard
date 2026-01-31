package com.sprk.security_demo_project.controller;

import com.sprk.security_demo_project.dto.AuthRequest;
import com.sprk.security_demo_project.entity.UserInfo;
import com.sprk.security_demo_project.service.JwtService;
import com.sprk.security_demo_project.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final UserInfoService userInfoService;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @GetMapping("/test")
    public String test() {
        return "testing security";
    }

    @GetMapping("/home")
    public String home() {
        return "Home Page after giving correct username and password security";
    }

    @GetMapping("/tester/test")
    @PreAuthorize("hasAuthority('ROLE_TESTER')")
    public String tester() {
        return "testing security I am having Tester Role";
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String showUser() {
        return "Testing security I am having User Role";
    }

    @GetMapping("/admin/show-admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String showAdmin() {
        return "I am admin";
    }


    @PostMapping("/signup")
    public UserInfo signup(@RequestBody UserInfo userInfo) {
        UserInfo signUpUserInfo = userInfoService.signUpUser(userInfo);

        return signUpUserInfo;
    }

    @PostMapping("/generate-token")
    public String generateToke(@RequestBody AuthRequest authRequest){

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        if(authenticate.isAuthenticated()){
            return jwtService.getToken(authenticate);
        }else{
            throw new UsernameNotFoundException("Invalid credentials");
        }
    }

    // check username and password correct or not



}
