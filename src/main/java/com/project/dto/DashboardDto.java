package com.project.dto;

public record DashboardDto(
        long totalPlayers,
        double avgPerformance,
        double topScore
) {
}