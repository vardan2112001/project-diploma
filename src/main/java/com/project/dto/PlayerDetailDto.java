package com.project.dto;

import lombok.Builder;

@Builder
public record PlayerDetailDto(
        Long id,
        String name,
        String club,
        String position,
        Double performanceScore,

        Integer appearances,
        Integer goals,
        Integer assists,
        Integer shotsOnTarget,
        Integer cleanSheets,
        Integer saves,
        Integer clusterId
) {}