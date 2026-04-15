package com.project.service;


import com.project.trainer.PositionTrainer;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class RegressionTrainingService {

    private final List<PositionTrainer> trainers;

    public void trainModels() {
        log.info("=== REGRESSION ML STARTED) ===");

        for (PositionTrainer trainer : trainers) {
            trainer.train();
        }

    }

}