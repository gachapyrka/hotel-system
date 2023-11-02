package com.example.hotel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class NoUserController {
    @GetMapping("/")
    public String index(Map<String, Object> model) {
        return "index";
    }

    @GetMapping("/registration-user")
    public String registrationUser(Map<String, Object> model) {
        return "registrationUser";
    }

    @GetMapping("/registration-employee")
    public String registrationEmployee(Map<String, Object> model) {
        return "registrationEmployee";
    }
}
