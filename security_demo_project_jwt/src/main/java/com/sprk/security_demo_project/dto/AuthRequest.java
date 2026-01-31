package com.sprk.security_demo_project.dto;

import lombok.Data;

@Data
public class AuthRequest {

    private String username;

    private String password;
}
