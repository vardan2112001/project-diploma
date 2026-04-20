package com.project.strategy.impl;

import com.project.entity.PlayerStats;
import com.project.strategy.PositionScoringStrategy;
import org.springframework.stereotype.Component;

@Component
public class  MidfielderScoringStrategy implements PositionScoringStrategy {

    private static final double CONSTANT = 0.33919;
    private static final double WEIGHT_GOALS = 0.53169;
    private static final double WEIGHT_ASSISTS = 0.10440;
    private static final double ZERO_SCORE = 0.0;
    private static final int ZERO_GOALS = 0;
    private static final int ZERO_ASSISTS = 0;
    private static final int ZERO_MATCHES = 0;
    private static final int MUL_BY_TEN = 10;

    @Override
    public boolean supports(String position) {
        String pos = position.toUpperCase();
        return pos.contains("MF") || pos.contains("MIDFIELDER") || pos.contains("AM") || pos.contains("CM") || pos.contains("DM");
    }

    @Override
    public double calculateScore(PlayerStats stats) {
        if (stats.getAppearances() == null || stats.getAppearances() == ZERO_MATCHES) {
            return ZERO_SCORE;
        }

        double matches = stats.getAppearances();
        double goals = (stats.getGoals() != null ? stats.getGoals() : ZERO_GOALS) / matches;
        double assists = (stats.getAssists() != null ? stats.getAssists() : ZERO_ASSISTS) / matches;

        double winProbability = CONSTANT + (goals * WEIGHT_GOALS) + (assists * WEIGHT_ASSISTS);

        return Math.max(winProbability * MUL_BY_TEN, ZERO_SCORE);
    }
}