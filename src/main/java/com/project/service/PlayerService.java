package com.project.service;

import com.project.dto.PlayerDetailDto;
import com.project.dto.PlayerResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlayerService {

    List<PlayerResponseDto> getTopPlayers(Pageable pageable);

    PlayerDetailDto getPlayerById(Long id);

    List<PlayerDetailDto> searchPlayersByName(String name);

    List<PlayerResponseDto> getPlayersByClusterRole(Integer clusterId, Pageable pageable);

}