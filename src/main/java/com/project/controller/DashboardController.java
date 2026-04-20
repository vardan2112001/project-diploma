package com.project.controller;

import com.project.dto.ClusterCountDto;
import com.project.dto.DashboardDto;
import com.project.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public DashboardDto getDashboard() {
        return dashboardService.getDashboard();
    }

    @GetMapping("/clusters")
    public List<Object[]> getClusterCounts() {
        return dashboardService.getClusterCounts();
    }
}