package com.project.service;

import com.project.dto.PlayerDetailDto;
import com.project.dto.PlayerResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PlayerService {

    Page<PlayerResponseDto> getTopPlayers(Pageable pageable);

    PlayerDetailDto getPlayerById(Long id);

    Page<PlayerDetailDto> searchPlayersByName(String name, Pageable pageable);

    Page<PlayerResponseDto> getPlayersByClusterRole(Integer clusterId, Pageable pageable);
}