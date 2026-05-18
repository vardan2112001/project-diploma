package com.project.strategy.impl;

import com.project.entity.PlayerStats;
import com.project.strategy.PositionScoringStrategy;
import org.springframework.stereotype.Component;

@Component
public class DefenderScoringStrategy implements PositionScoringStrategy {

    private static final double CONSTANT = 0.21330;
    private static final double WEIGHT_CLEAN_SHEETS = 0.77150;
    private static final double WEIGHT_ASSISTS = 0.58046;
    private static final double ZERO_SCORE = 0.0;
    private static final int ZERO_APPEARANCES = 0;
    private static final int ZERO_VALUE = 0;
    private static final int SCORE_SCALE = 10;

    @Override
    public boolean supports(String position) {
        String pos = position.toUpperCase();
        return pos.contains("DF") || pos.contains("DEFENDER")
                || pos.contains("CB") || pos.contains("LB") || pos.contains("RB");
    }

    @Override
    public double calculateScore(PlayerStats stats) {
        if (stats.getAppearances() == null || stats.getAppearances() == ZERO_APPEARANCES) {
            return ZERO_SCORE;
        }

        double matches = stats.getAppearances();
        double cleanSheets = (stats.getCleanSheets() != null ? stats.getCleanSheets() : ZERO_VALUE) / matches;
        double assists = (stats.getAssists() != null ? stats.getAssists() : ZERO_VALUE) / matches;

        double winProbability = CONSTANT + (cleanSheets * WEIGHT_CLEAN_SHEETS) + (assists * WEIGHT_ASSISTS);

        return Math.max(winProbability * SCORE_SCALE, ZERO_SCORE);
    }
}
