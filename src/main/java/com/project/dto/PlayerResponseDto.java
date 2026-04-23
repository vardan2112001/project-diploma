package com.project.dto;

import lombok.Builder;

@Builder
public record PlayerResponseDto(

        Long id,
        String name,
        String club,
        String position,
        Integer age,
        Double performanceScore,
        Integer clusterId) {}
