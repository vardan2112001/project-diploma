package com.project.dto;

import lombok.Builder;

@Builder
public record CsvPlayerDto(
        String name,
        Integer jerseyNumber,
        String teamName,
        String position,
        String nationality,
        Integer age,
        Integer appearances,
        Integer wins,
        Integer goals,
        Integer shotsOnTarget,
        Integer cleanSheets,
        Integer assists,
        Integer saves) {
}
