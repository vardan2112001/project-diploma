package com.project.mapper;

import com.project.dto.PlayerDetailDto;
import com.project.dto.PlayerResponseDto;
import com.project.entity.Player;
import com.project.entity.PlayerStats;

public class PlayerMapper {
    private static final Double ZERO_SCORE = 0.0;
    private static final Integer ZERO_STAT = 0;

    public static PlayerResponseDto toDto(Player player) {
        return PlayerResponseDto.builder().
                id(player.getId())
                .name(player.getName())
                .club(player.getTeam() != null ? player.getTeam().getName() : "No club")
                .position(player.getPosition())
                .age(player.getAge())
                .performanceScore(getScore(player))
                .clusterId(getClusterId(player))
                .build();
    }


    public static PlayerDetailDto toDetailDto(Player player) {
        PlayerStats stats = player.getStats();

        return PlayerDetailDto.builder()
                .id(player.getId())
                .name(player.getName())
                .club(player.getTeam() != null ? player.getTeam().getName() : "No club ")
                .position(player.getPosition())
                .performanceScore(getScore(player))
                .appearances(stats != null && stats.getAppearances() != null ? stats.getAppearances() : ZERO_STAT)
                .goals(stats != null && stats.getGoals() != null ? stats.getGoals() : ZERO_STAT)
                .assists(stats != null && stats.getAssists() != null ? stats.getAssists() : ZERO_STAT)
                .shotsOnTarget(stats != null && stats.getShotsOnTarget() != null ? stats.getShotsOnTarget() : ZERO_STAT)
                .cleanSheets(stats != null && stats.getCleanSheets() != null ? stats.getCleanSheets() : ZERO_STAT)
                .saves(stats != null && stats.getSaves() != null ? stats.getSaves() : ZERO_STAT)
                .clusterId(getClusterId(player))
                .build();
    }

    private static Double getScore(Player player) {
        return player.getStats() != null && player.getStats().getPerformanceScore() != null
                ? player.getStats().getPerformanceScore()
                : ZERO_SCORE;
    }

    private static Integer getClusterId(Player player) {
        return player.getStats() != null && player.getStats().getClusterId() != null
                ? player.getStats().getClusterId()
                : null;
    }

}