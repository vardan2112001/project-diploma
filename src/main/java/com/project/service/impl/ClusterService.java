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

import java.util.ArrayList;
import java.util.List;

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

        StatMaxima maxima = computeMaxima(players);

        List<PlayerWrapper> clusterInput = new ArrayList<>(players.size());
        for (Player p : players) {
            clusterInput.add(new PlayerWrapper(p, maxima));
        }

        KMeansPlusPlusClusterer<PlayerWrapper> clusterer =
                new KMeansPlusPlusClusterer<>(NUM_CLUSTERS, MAX_ITERATIONS);

        List<CentroidCluster<PlayerWrapper>> clusters = clusterer.cluster(clusterInput);

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

    private static StatMaxima computeMaxima(List<Player> players) {
        double maxGoals = 0;
        double maxAssists = 0;
        double maxCleanSheets = 0;
        double maxShots = 0;

        for (Player p : players) {
            PlayerStats s = p.getStats();
            maxGoals = Math.max(maxGoals, s.getGoals());
            maxAssists = Math.max(maxAssists, s.getAssists());
            maxCleanSheets = Math.max(maxCleanSheets, s.getCleanSheets());
            maxShots = Math.max(maxShots, s.getShotsOnTarget());
        }

        return new StatMaxima(
                safeMax(maxGoals),
                safeMax(maxAssists),
                safeMax(maxCleanSheets),
                safeMax(maxShots));
    }

    private static double safeMax(double value) {
        return value == 0 ? DEFAULT_MAX_VALUE : value;
    }

    private record StatMaxima(double goals, double assists, double cleanSheets, double shots) {
    }

    private static class PlayerWrapper implements Clusterable {

        private final double[] points;
        @Getter
        private final Player player;

        PlayerWrapper(Player player, StatMaxima maxima) {
            this.player = player;
            PlayerStats s = player.getStats();

            double normGoals = s.getGoals() / maxima.goals();
            double normAssists = s.getAssists() / maxima.assists();
            double normCleanSheets = s.getCleanSheets() / maxima.cleanSheets();
            double normShots = s.getShotsOnTarget() / maxima.shots();

            double isGk = (player.getPosition() != null && player.getPosition().toUpperCase().contains("GK")) ? 1.0 : 0.0;

            this.points = new double[]{normGoals, normAssists, normCleanSheets, normShots, isGk};
        }

        @Override
        public double[] getPoint() {
            return points;
        }
    }
}
