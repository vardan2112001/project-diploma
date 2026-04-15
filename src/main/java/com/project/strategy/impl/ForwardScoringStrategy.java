package com.project.strategy.impl;

import com.project.entity.PlayerStats;
import com.project.strategy.PositionScoringStrategy;
import org.springframework.stereotype.Component;

@Component
public class ForwardScoringStrategy implements PositionScoringStrategy {


    private static final double CONSTANT = 0.26077;
    private static final double WEIGHT_GOALS = 0.53613;
    private static final double WEIGHT_ASSISTS = 0.98370;
    private static final double WEIGHT_SHOTS = -0.13763;
    private static final double ZERO_SCORE = 0.0;
    private static final int  ZERO_APPEARANCES = 0;
    private static final int  ZERO_GOALS = 0;
    private static final int  ZERO_ASSISTS = 0;
    private static final int  ZERO_SHOTS_ON_TARGET = 0;
    private static final int MUL_BY_TEN = 10;

    @Override
    public boolean supports(String position) {
        String pos = position.toUpperCase();
        return pos.contains("FW") || pos.contains("FORWARD");
    }

    @Override
    public double calculateScore(PlayerStats stats) {
        if (stats.getAppearances() == null || stats.getAppearances() == ZERO_APPEARANCES) return ZERO_SCORE;

        double matches = stats.getAppearances();
        double goals = (stats.getGoals() != null ? stats.getGoals() : ZERO_GOALS) / matches;
        double assists = (stats.getAssists() != null ? stats.getAssists() : ZERO_ASSISTS) / matches;
        double shots = (stats.getShotsOnTarget() != null ? stats.getShotsOnTarget() : ZERO_SHOTS_ON_TARGET) / matches;


        double winProbability = CONSTANT + (goals * WEIGHT_GOALS) + (assists * WEIGHT_ASSISTS) + (shots * WEIGHT_SHOTS);


        double finalScore = winProbability * MUL_BY_TEN;
        return Math.max(finalScore, ZERO_SCORE);
    }
}