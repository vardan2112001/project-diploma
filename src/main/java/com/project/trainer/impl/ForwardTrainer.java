package com.project.trainer.impl;

import com.project.entity.Player;
import com.project.entity.PlayerStats;
import com.project.exceptions.PlayerNotFoundException;
import com.project.repository.PlayerRepository;
import com.project.trainer.PositionTrainer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class ForwardTrainer implements PositionTrainer {
    private final PlayerRepository playerRepository;

    private static final Integer  MIN_APPEARANCES = 5;
    private static final int PARAMS_FOR_FEATURES = 3;
    private static final int ZERO_WINS = 0;
    private static final int ZERO_GOALS = 0;
    private static final int ZERO_ASSISTS = 0;
    private static final int ZERO_SHOTS_ON_TARGET = 0;

    @Override
    public void train() {

        List<Player> forwards = playerRepository.findForwards(MIN_APPEARANCES);

        if (forwards.isEmpty()){
           log.warn("No forwards found for training");
        }

        double[][] forwardFeaturesMatrix = new double[forwards.size()][PARAMS_FOR_FEATURES];
        double[] targetWinRates = new double[forwards.size()];

        for (int i = 0; i < forwards.size(); i++) {
            PlayerStats stats = forwards.get(i).getStats();
            double matches = Math.max(stats.getAppearances(), 1);

            targetWinRates[i] = (stats.getWins() != null ? stats.getWins() : ZERO_WINS) / matches;

            forwardFeaturesMatrix[i][0] = (stats.getGoals() != null ? stats.getGoals() : ZERO_GOALS) / matches;
            forwardFeaturesMatrix[i][1] = (stats.getAssists() != null ? stats.getAssists() : ZERO_ASSISTS) / matches;
            forwardFeaturesMatrix[i][2] = (stats.getShotsOnTarget() != null ? stats.getShotsOnTarget() : ZERO_SHOTS_ON_TARGET) / matches;
        }

        OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
        regression.newSampleData(targetWinRates, forwardFeaturesMatrix);
        double[] beta = regression.estimateRegressionParameters();

        log.info("--- FORWARDS (FW) ---");
        log.info("Basic chance for win (Constant): " + beta[0]);
        log.info("Weight for  Goals (x1): " + beta[1]);
        log.info("Weight for  Assists (x2): " + beta[2]);
        log.info("Weight for  Shots (x3): " + beta[3]);
        log.info("-----------------------");
    }
}
