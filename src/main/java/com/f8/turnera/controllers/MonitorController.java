package com.f8.turnera.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MonitorController {

    @GetMapping("/monitor")
    public String monitor() {
        return "Api Turnera OK!";
    }
}