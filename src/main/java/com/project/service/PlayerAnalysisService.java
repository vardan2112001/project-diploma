package com.project.service;

import com.project.entity.Player;
import com.project.entity.PlayerStats;
import com.project.strategy.PositionScoringStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PlayerAnalysisService {

    private static final Double ZERO_SCORE = 0.0;
    private static final Integer ZERO_MATCHES = 0;
    private final List<PositionScoringStrategy> strategies;

    public double calculatePerformanceScore(Player player) {
        PlayerStats stats = player.getStats();
        if (stats == null || stats.getAppearances() == null || stats.getAppearances().equals(ZERO_MATCHES)) {
            return ZERO_SCORE;
        }

        String position = player.getPosition();
        if (position == null) {
            return ZERO_SCORE;
        }
        position = position.toUpperCase();

        for (PositionScoringStrategy strategy : strategies) {
            if (strategy.supports(position)) {
                return strategy.calculateScore(stats);
            }
        }

        return ZERO_SCORE;
    }
}