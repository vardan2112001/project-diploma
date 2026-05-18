package com.project.service.impl;

import com.project.entity.Player;
import com.project.entity.PlayerStats;
import com.project.service.AnalysisService;
import com.project.strategy.PositionScoringStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerAnalysisService implements AnalysisService {

    private static final double ZERO_SCORE = 0.0;
    private static final int ZERO_MATCHES = 0;

    private final List<PositionScoringStrategy> strategies;

    @Override
    public double calculatePerformanceScore(Player player) {
        PlayerStats stats = player.getStats();
        if (stats == null || stats.getAppearances() == null || stats.getAppearances() == ZERO_MATCHES) {
            return ZERO_SCORE;
        }

        String position = player.getPosition();
        if (position == null) {
            return ZERO_SCORE;
        }
        String upperPosition = position.toUpperCase();

        return strategies.stream()
                .filter(strategy -> strategy.supports(upperPosition))
                .findFirst()
                .map(strategy -> strategy.calculateScore(stats))
                .orElse(ZERO_SCORE);
    }
}
