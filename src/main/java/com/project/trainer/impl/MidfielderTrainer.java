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
public class MidfielderTrainer implements PositionTrainer {
    private final PlayerRepository playerRepository;

    private static final Integer  MIN_APPEARANCES = 5;
    private static final int PARAMS_FOR_FEATURES = 2;
    private static final int ZERO_WINS = 0;
    private static final int ZERO_GOALS = 0;
    private static final int ZERO_ASSISTS = 0;

    @Override
    public void train() {
        List<Player> midfielders = playerRepository.findMidfielders(MIN_APPEARANCES);
        if (midfielders.isEmpty()){
              log.warn("No midfielders  found for training");
        }

        double[][] midfielderFeaturesMatrix = new double[midfielders.size()][PARAMS_FOR_FEATURES];
        double[] targetWinRates = new double[midfielders.size()];

        for (int i = 0; i < midfielders.size(); i++) {
            PlayerStats stats = midfielders.get(i).getStats();
            double matches = Math.max(stats.getAppearances(), 1);

            targetWinRates[i] = (stats.getWins() != null ? stats.getWins() : ZERO_WINS) / matches;

            midfielderFeaturesMatrix[i][0] = (stats.getGoals() != null ? stats.getGoals() : ZERO_GOALS) / matches;
            midfielderFeaturesMatrix[i][1] = (stats.getAssists() != null ? stats.getAssists() : ZERO_ASSISTS) / matches;
        }

        OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
        regression.newSampleData(targetWinRates, midfielderFeaturesMatrix);
        double[] beta = regression.estimateRegressionParameters();

        log.info("--- MIDFIELDERS (MF) ---");
        log.info("Minimum chance for Win (Constant): " + beta[0]);
        log.info("Weight for  Goals(x1): " + beta[1]);
        log.info("Weight for  Assists(x2): " + beta[2]);
        log.info("--------------------------");
    }
}
