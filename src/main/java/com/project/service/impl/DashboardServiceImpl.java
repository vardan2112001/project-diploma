package com.project.service.impl;

import com.project.dto.DashboardDto;
import com.project.repository.PlayerRepository;
import com.project.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
@RequiredArgsConstructor
public class DashboardServiceImpl  implements DashboardService {

    private static final double ZERO_SCORE  = 0.0;
    private static final double MUL_BY_ONE_HUNDRED = 100.0;
    private static final double DIV_BY_ONE_HUNDRED = 100.0;
    private final PlayerRepository playerRepository;

    @Override
    public DashboardDto getDashboard() {
        long totalPlayers =playerRepository.count();
        Double avgPerformanceScore = playerRepository.getAveragePerformanceScore();
        Double topPerformanceScore = playerRepository.getTopPerformanceScore();

        return new DashboardDto(
                totalPlayers,
                avgPerformanceScore  != null ? round(avgPerformanceScore) :ZERO_SCORE,
                topPerformanceScore != null ? round(topPerformanceScore) : ZERO_SCORE
        );
    }

    @Override
    public List<Object[]> getClusterCounts() {
        return playerRepository.getClusterCounts();
    }
    private double round(Double value) {
        return Math.round(value * MUL_BY_ONE_HUNDRED) / DIV_BY_ONE_HUNDRED;
    }

}
