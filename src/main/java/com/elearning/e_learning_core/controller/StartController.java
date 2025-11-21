package com.elearning.e_learning_core.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elearning.e_learning_core.Dtos.StatsDTO;
import com.elearning.e_learning_core.service.StatsService;

@RestController
@RequestMapping("/dashboard")
public class StartController {

    private final StatsService dashboardService;

    public StartController(StatsService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/stats")
    public StatsDTO getStats() {
        return dashboardService.getDashboardStats();
    }

}