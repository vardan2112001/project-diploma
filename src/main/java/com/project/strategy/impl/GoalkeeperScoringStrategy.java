package com.project.strategy.impl;

import com.project.entity.PlayerStats;
import com.project.strategy.PositionScoringStrategy;
import org.springframework.stereotype.Component;

@Component
public class GoalkeeperScoringStrategy implements PositionScoringStrategy {

    private static final double CONSTANT = 0.53851;
    private static final double WEIGHT_SAVES = -0.12022;
    private static final double WEIGHT_CLEAN_SHEETS = 0.67280;
    private static final double ZERO_SCORE = 0.0;
    private static final int ZERO_APPEARANCES = 0;
    private static final int ZERO_VALUE = 0;
    private static final int SCORE_SCALE = 10;

    @Override
    public boolean supports(String position) {
        String pos = position.toUpperCase();
        return pos.contains("GK") || pos.contains("GOALKEEPER");
    }

    @Override
    public double calculateScore(PlayerStats stats) {
        if (stats.getAppearances() == null || stats.getAppearances() == ZERO_APPEARANCES) {
            return ZERO_SCORE;
        }

        double matches = stats.getAppearances();
        double saves = (stats.getSaves() != null ? stats.getSaves() : ZERO_VALUE) / matches;
        double cleanSheets = (stats.getCleanSheets() != null ? stats.getCleanSheets() : ZERO_VALUE) / matches;

        double winProbability = CONSTANT + (saves * WEIGHT_SAVES) + (cleanSheets * WEIGHT_CLEAN_SHEETS);

        return Math.max(winProbability * SCORE_SCALE, ZERO_SCORE);
    }
}
