package com.project.trainer.impl;

import com.project.entity.Player;
import com.project.entity.PlayerStats;
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
public class GoalkeeperTrainer implements PositionTrainer {

    private static final Integer MIN_APPEARANCES = 5;
    private static final int PARAMS_FOR_FEATURES = 2;
    private static final int ZERO_WINS = 0;
    private static final int ZERO_SAVES = 0;
    private static final int ZERO_CLEAN_SHEETS = 0;

    private final PlayerRepository playerRepository;

    @Override
    public void train() {
        List<Player> goalkeepers = playerRepository.findGoalkeepers(MIN_APPEARANCES);

        if (goalkeepers.isEmpty()) {
            log.warn("No goalkeepers found for training");
            return;
        }

        double[][] goalkeeperFeaturesMatrix = new double[goalkeepers.size()][PARAMS_FOR_FEATURES];
        double[] targetWinRates = new double[goalkeepers.size()];

        for (int i = 0; i < goalkeepers.size(); i++) {
            PlayerStats stats = goalkeepers.get(i).getStats();
            double matches = Math.max(stats.getAppearances(), 1);

            targetWinRates[i] = (stats.getWins() != null ? stats.getWins() : ZERO_WINS) / matches;

            goalkeeperFeaturesMatrix[i][0] = (stats.getSaves() != null ? stats.getSaves() : ZERO_SAVES) / matches;
            goalkeeperFeaturesMatrix[i][1] = (stats.getCleanSheets() != null ? stats.getCleanSheets() : ZERO_CLEAN_SHEETS) / matches;
        }

        OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
        regression.newSampleData(targetWinRates, goalkeeperFeaturesMatrix);
        double[] beta = regression.estimateRegressionParameters();

        log.info("--- GOALKEEPERS ---");
        log.info("Chance for Win (Constant): {}", beta[0]);
        log.info("Weight for Saves (x1): {}", beta[1]);
        log.info("Weight for Clean-Sheets (x2): {}", beta[2]);
        log.info("--------------------");
    }
}
