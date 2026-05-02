package com.project.mapper;

import com.project.dto.PlayerDetailDto;
import com.project.dto.PlayerResponseDto;
import com.project.entity.Player;
import com.project.entity.PlayerStats;

import java.util.function.Function;

public class PlayerMapper {

    private static final Double ZERO_SCORE = 0.0;
    private static final Integer ZERO_STAT = 0;
    private static final String NO_CLUB = "No club";

    public static PlayerResponseDto toDto(Player player) {
        return PlayerResponseDto.builder()
                .id(player.getId())
                .name(player.getName())
                .club(clubName(player))
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
                .club(clubName(player))
                .position(player.getPosition())
                .age(player.getAge())
                .performanceScore(getScore(player))
                .appearances(safeStat(stats, PlayerStats::getAppearances))
                .goals(safeStat(stats, PlayerStats::getGoals))
                .assists(safeStat(stats, PlayerStats::getAssists))
                .shotsOnTarget(safeStat(stats, PlayerStats::getShotsOnTarget))
                .cleanSheets(safeStat(stats, PlayerStats::getCleanSheets))
                .saves(safeStat(stats, PlayerStats::getSaves))
                .clusterId(getClusterId(player))
                .build();
    }

    private static String clubName(Player player) {
        return player.getTeam() != null ? player.getTeam().getName() : NO_CLUB;
    }

    private static Double getScore(Player player) {
        PlayerStats stats = player.getStats();
        if (stats == null || stats.getPerformanceScore() == null) {
            return ZERO_SCORE;
        }
        return stats.getPerformanceScore();
    }

    private static Integer getClusterId(Player player) {
        PlayerStats stats = player.getStats();
        return stats != null ? stats.getClusterId() : null;
    }

    private static Integer safeStat(PlayerStats stats, Function<PlayerStats, Integer> getter) {
        if (stats == null) {
            return ZERO_STAT;
        }
        Integer value = getter.apply(stats);
        return value != null ? value : ZERO_STAT;
    }
}
