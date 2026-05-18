package com.project.service.impl;

import com.project.dto.CsvPlayerDto;
import com.project.entity.Player;
import com.project.entity.PlayerStats;
import com.project.entity.Team;
import com.project.mapper.CsvMapper;
import com.project.repository.PlayerRepository;
import com.project.repository.TeamRepository;
import com.project.service.AnalysisService;
import com.project.service.ClusterizationService;
import com.project.service.DataSeeder;
import com.project.service.RegressionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DatabaseSeederService implements DataSeeder {

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final AnalysisService playerAnalysisService;
    private final ClusterizationService clusterService;
    private final RegressionService regressionService;

    @Transactional
    @Override
    public void seedDatabase(List<CsvPlayerDto> parsedPlayers) {
        if (playerRepository.existsByIdIsNotNull()) {
            log.info("====== DB IS FULL UNABLE IMPORT ======");
            return;
        }

        Map<String, Team> teamCache = new HashMap<>();

        for (CsvPlayerDto dto : parsedPlayers) {
            Team team = resolveTeam(dto.teamName(), teamCache);

            Player player = CsvMapper.toPlayerEntity(dto, team);
            PlayerStats stats = CsvMapper.toPlayerStatsEntity(dto, player);

            player.setStats(stats);
            stats.setPerformanceScore(playerAnalysisService.calculatePerformanceScore(player));

            playerRepository.save(player);
        }
        log.info("Players loaded in DB successfully");

        clusterService.performClustering();
        regressionService.trainModels();

        log.info("====== DB INITIALIZED SUCCESSFULLY ======");
    }

    private Team resolveTeam(String teamName, Map<String, Team> teamCache) {
        return teamCache.computeIfAbsent(teamName, name ->
                teamRepository.findByName(name).orElseGet(() -> {
                    Team newTeam = new Team();
                    newTeam.setName(name);
                    return teamRepository.save(newTeam);
                }));
    }
}
