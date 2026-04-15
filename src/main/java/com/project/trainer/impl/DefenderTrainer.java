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

@RequiredArgsConstructor
@Slf4j
@Component
public class DefenderTrainer  implements PositionTrainer {
    private final PlayerRepository playerRepository;

    private static final Integer  MIN_APPEARANCES = 5;
    private static final int PARAMS_FOR_FEATURES = 2;
    private static final int ZERO_WINS = 0;
    private static final int ZERO_CLEAN_SHEETS = 0;
    private static final int ZERO_ASSISTS = 0;

    @Override
    public void train() {
        List<Player> defenders = playerRepository.findDefenders(MIN_APPEARANCES);

        if (defenders.isEmpty()){
             log.warn("No defenders found for training");
        }

        double[][] defenderFeaturesMatrix = new double[defenders.size()][PARAMS_FOR_FEATURES];
        double[] targetWinRates = new double[defenders.size()];

        for (int i = 0; i < defenders.size(); i++) {
            PlayerStats stats = defenders.get(i).getStats();
            double matches = Math.max(stats.getAppearances(), 1);

            targetWinRates[i] = (stats.getWins() != null ? stats.getWins() : ZERO_WINS) / matches;

            defenderFeaturesMatrix[i][0] = (stats.getCleanSheets() != null ? stats.getCleanSheets() : ZERO_CLEAN_SHEETS) / matches;
            defenderFeaturesMatrix[i][1] = (stats.getAssists() != null ? stats.getAssists() : ZERO_ASSISTS) / matches;
        }

        OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
        regression.newSampleData(targetWinRates, defenderFeaturesMatrix);
        double[] beta = regression.estimateRegressionParameters();

        log.info("--- DEFENDERS (DF) ---");
        log.info("Minimum chance  for Win (Constant): " + beta[0]);
        log.info("Weight for  Clean-Sheets (x1): " + beta[1]);
        log.info("Weight for  Assists  (x2): " + beta[2]);
        log.info("-----------------------");
    }
}
