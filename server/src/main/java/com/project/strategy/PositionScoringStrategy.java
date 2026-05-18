package com.project.strategy;

import com.project.entity.PlayerStats;

public interface PositionScoringStrategy {

    boolean supports(String position);
    double calculateScore(PlayerStats stats);

}
