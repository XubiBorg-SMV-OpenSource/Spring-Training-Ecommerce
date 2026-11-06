package com.xbbg.springlab.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/api/health")
    public Map<String, String> health() {

        return Map.of(
            "application", "XBBG Commerce",
            "status", "UP",
            "framework", "Spring Framework"
        );
    }
}
