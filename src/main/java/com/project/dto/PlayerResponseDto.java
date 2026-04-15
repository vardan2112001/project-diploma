package com.project.dto;

import lombok.Builder;

@Builder
public record PlayerResponseDto(
        String name,
        String club,
        String position,
        Integer age,
        Double performanceScore,
        Integer clusterId) {}
