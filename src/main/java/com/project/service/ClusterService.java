package com.project.service;

import com.project.entity.Player;
import com.project.entity.PlayerStats;
import com.project.repository.PlayerRepository;
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

public class ClusterService {


    private static final int NUM_CLUSTERS = 4;
    private static final int MAX_ITERATIONS = 1000;
    private static final double DEFAULT_MAX_VALUE = 1.0;

    private final PlayerRepository playerRepository;

    @Transactional
    public void performClustering() {
        log.info("Starts K-Means algorithm...");

        List<Player> players = playerRepository.findAllPlayersWithStats();

        if (players.isEmpty()) {
            log.warn("No data for clustering.");
            return;
        }


        double maxGoals = players.stream().mapToDouble(p -> p.getStats().getGoals()).max().orElse(DEFAULT_MAX_VALUE);
        double maxAssists = players.stream().mapToDouble(p -> p.getStats().getAssists()).max().orElse(DEFAULT_MAX_VALUE);
        double maxCleanSheets = players.stream().mapToDouble(p -> p.getStats().getCleanSheets()).max().orElse(DEFAULT_MAX_VALUE);
        double maxApps = players.stream().mapToDouble(p -> p.getStats().getAppearances()).max().orElse(DEFAULT_MAX_VALUE);


        List<PlayerWrapper> clusterInput = players.stream()
                .map(p -> new PlayerWrapper(p, maxGoals, maxAssists, maxCleanSheets, maxApps))
                .collect(Collectors.toList());


        KMeansPlusPlusClusterer<PlayerWrapper> clusterer = new KMeansPlusPlusClusterer<>(NUM_CLUSTERS, MAX_ITERATIONS);
        List<CentroidCluster<PlayerWrapper>> clusters = clusterer.cluster(clusterInput);


        for (int i = 0; i < clusters.size(); i++) {
            CentroidCluster<PlayerWrapper> cluster = clusters.get(i);
            double[] center = cluster.getCenter().getPoint();

            log.info("Cluster №{}: Players = {} | Center: Goals={}, Аssists={}, Clean sheets={}, Games={}",
                    i,
                    cluster.getPoints().size(),
                    String.format("%.2f", center[0]),
                    String.format("%.2f", center[1]),
                    String.format("%.2f", center[2]),
                    String.format("%.2f", center[3]));

            for (PlayerWrapper wrapper : cluster.getPoints()) {
                wrapper.getPlayer().getStats().setClusterId(i);
            }
        }


        playerRepository.saveAll(players);
        log.info("====== PLAYERS ANALYZING SUCCESFULLY  FINISHED======");
    }



    private static class PlayerWrapper implements Clusterable {
        private final double[] points;
        private static final int EQUALS_TO_ZERO = 0;
        @Getter
        private final Player player;

        public PlayerWrapper(Player player, double maxG, double maxA, double maxCS, double maxApp) {
            this.player = player;
            PlayerStats s = player.getStats();

            double normGoals = s.getGoals() / (maxG == EQUALS_TO_ZERO ? DEFAULT_MAX_VALUE : maxG);
            double normAssists = s.getAssists() / (maxA == EQUALS_TO_ZERO ? DEFAULT_MAX_VALUE : maxA);
            double normCleanSheets = s.getCleanSheets() / (maxCS == EQUALS_TO_ZERO ? DEFAULT_MAX_VALUE : maxCS);
            double normApps = s.getAppearances() / (maxApp == EQUALS_TO_ZERO ? DEFAULT_MAX_VALUE : maxApp);

            double isGk = (player.getPosition() != null && player.getPosition().toUpperCase().contains("GK")) ? 1.0 : 0.0;

            this.points = new double[]{normGoals, normAssists, normCleanSheets, normApps, isGk};
        }

        @Override
        public double[] getPoint() {
            return points;
        }

    }
}