package com.project.service.impl;

import com.project.entity.Player;
import com.project.entity.PlayerStats;
import com.project.repository.PlayerRepository;
import com.project.service.ClusterizationService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor

public class ClusterService implements ClusterizationService {

    private static final int NUM_CLUSTERS = 4;
    private static final int MAX_ITERATIONS = 1000;
    private static final double DEFAULT_MAX_VALUE = 1.0;

    private final PlayerRepository playerRepository;

    @Transactional
    @Override
    public void performClustering() {
        log.info("Starts K-Means algorithm...");

        List<Player> players = playerRepository.findAllPlayersWithStats();

        if (players.isEmpty()) {
            log.warn("No data for clustering.");
            return;
        }

        double maxGoals = players.stream()
                .mapToDouble(p -> p.getStats().getGoals())
                .max()
                .orElse(DEFAULT_MAX_VALUE);

        double maxAssists = players.stream()
                .mapToDouble(p -> p.getStats().getAssists())
                .max()
                .orElse(DEFAULT_MAX_VALUE);

        double maxCleanSheets = players.stream()
                .mapToDouble(p -> p.getStats().getCleanSheets())
                .max()
                .orElse(DEFAULT_MAX_VALUE);

        double maxShots = players.stream()
                .mapToDouble(p -> p.getStats().getShotsOnTarget())
                .max()
                .orElse(DEFAULT_MAX_VALUE);

        List<PlayerWrapper> clusterInput = players.stream()
                .map(p -> new PlayerWrapper(
                        p,
                        maxGoals,
                        maxAssists,
                        maxCleanSheets,
                        maxShots
                ))
                .collect(Collectors.toList());

        KMeansPlusPlusClusterer<PlayerWrapper> clusterer =
                new KMeansPlusPlusClusterer<>(NUM_CLUSTERS, MAX_ITERATIONS);

        List<CentroidCluster<PlayerWrapper>> clusters =
                clusterer.cluster(clusterInput);

        for (int i = 0; i < clusters.size(); i++) {

            CentroidCluster<PlayerWrapper> cluster = clusters.get(i);
            double[] center = cluster.getCenter().getPoint();

            log.info("""
                Cluster {}:
                Players = {}
                Center Goals = {}
                Center Assists = {}
                Center CleanSheets = {}
                Center Shots = {}
                Center IsGK = {}
                """,
                    i,
                    cluster.getPoints().size(),
                    String.format("%.4f", center[0]),
                    String.format("%.4f", center[1]),
                    String.format("%.4f", center[2]),
                    String.format("%.4f", center[3]),
                    String.format("%.4f", center[4]));

            for (PlayerWrapper wrapper : cluster.getPoints()) {
                wrapper.getPlayer().getStats().setClusterId(i);
            }
        }

        playerRepository.saveAll(players);

        log.info("====== PLAYERS ANALYZING SUCCESSFULLY FINISHED ======");
    }

    private static class PlayerWrapper implements Clusterable {
        private static final int EQUALS_TO_ZERO = 0;
        private final double[] points;
        @Getter
        private final Player player;

        public PlayerWrapper(Player player, double maxG, double maxA, double maxCS, double maxShots) {
            this.player = player;
            PlayerStats s = player.getStats();

            double normGoals = s.getGoals() / (maxG == EQUALS_TO_ZERO ? DEFAULT_MAX_VALUE : maxG);
            double normAssists = s.getAssists() / (maxA == EQUALS_TO_ZERO ? DEFAULT_MAX_VALUE : maxA);
            double normCleanSheets = s.getCleanSheets() / (maxCS == EQUALS_TO_ZERO ? DEFAULT_MAX_VALUE : maxCS);
            double normShots = s.getShotsOnTarget() / (maxShots == EQUALS_TO_ZERO ? DEFAULT_MAX_VALUE : maxShots);

            double isGk = (player.getPosition() != null && player.getPosition().toUpperCase().contains("GK")) ? 1.0 : 0.0;

            this.points = new double[]{normGoals, normAssists, normCleanSheets, normShots, isGk};
        }

        @Override
        public double[] getPoint() {
            return points;
        }

    }
}