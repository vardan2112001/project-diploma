package com.project.service.impl;

import com.project.service.RegressionService;
import com.project.trainer.PositionTrainer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegressionTrainingService implements RegressionService {

    private final List<PositionTrainer> trainers;

    @Override
    public void trainModels() {
        log.info("=== REGRESSION ML STARTED ===");

        for (PositionTrainer trainer : trainers) {
            trainer.train();
        }
    }
}
