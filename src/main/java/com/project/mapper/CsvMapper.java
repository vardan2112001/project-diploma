package com.project.mapper;

import com.project.dto.CsvPlayerDto;
import com.project.entity.Player;
import com.project.entity.PlayerStats;
import com.project.entity.Team;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CsvMapper {

    private static final int IDX_NAME = 0;
    private static final int IDX_JERSEY = 1;
    private static final int IDX_TEAM = 2;
    private static final int IDX_POSITION = 3;
    private static final int IDX_NATIONALITY = 4;
    private static final int IDX_AGE = 5;
    private static final int IDX_APPEARANCES = 6;
    private static final int IDX_WINS = 7;
    private static final int IDX_GOALS = 9;
    private static final int IDX_SHOTS_ON_TARGET = 17;
    private static final int IDX_CLEAN_SHEETS = 21;
    private static final int IDX_ASSISTS = 39;
    private static final int IDX_SAVES = 47;


    public static CsvPlayerDto toDto(String[] data) {
        return CsvPlayerDto.builder()
                .name(data[IDX_NAME].trim())
                .jerseyNumber(parseIntSafe(data[IDX_JERSEY]))
                .teamName(data[IDX_TEAM].trim())
                .position(data[IDX_POSITION].trim())
                .nationality(data[IDX_NATIONALITY].trim())
                .age(parseIntSafe(data[IDX_AGE]))
                .appearances(parseIntSafe(data[IDX_APPEARANCES]))
                .wins(parseIntSafe(data[IDX_WINS]))
                .goals(parseIntSafe(data[IDX_GOALS]))
                .shotsOnTarget(parseIntSafe(data[IDX_SHOTS_ON_TARGET]))
                .cleanSheets(parseIntSafe(data[IDX_CLEAN_SHEETS]))
                .assists(parseIntSafe(data[IDX_ASSISTS]))
                .saves(parseIntSafe(data[IDX_SAVES]))
                .build();
    }

    public static Player toPlayerEntity(CsvPlayerDto dto, Team team) {
        return Player.builder().
                name(dto.name()).
                jerseyNumber(dto.jerseyNumber()).
                position(dto.position()).
                nationality(dto.nationality()).
                age(dto.age()).
                team(team).build();
    }

    public static PlayerStats toPlayerStatsEntity(CsvPlayerDto dto, Player player) {
        return PlayerStats.builder()
                .appearances(dto.appearances())
                .wins(dto.wins())
                .goals(dto.goals())
                .shotsOnTarget(dto.shotsOnTarget())
                .cleanSheets(dto.cleanSheets())
                .assists(dto.assists())
                .saves(dto.saves())
                .player(player)
                .build();
    }

    private static Integer parseIntSafe(String value) {
        if (value == null || value.isBlank()) return 0;
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            log.warn("Parsing problem  '{}'. Value is 0.", value);
            return 0;
        }
    }
}