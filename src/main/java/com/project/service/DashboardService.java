package com.project.service;

import com.project.dto.DashboardDto;

import java.util.List;

public interface DashboardService {
    DashboardDto getDashboard();
    List<Object[]> getClusterCounts();
}
