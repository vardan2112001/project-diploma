package com.project.service.impl;

import com.project.dto.PlayerDetailDto;
import com.project.dto.PlayerResponseDto;
import com.project.exceptions.PlayerNotFoundException;
import com.project.mapper.PlayerMapper;
import com.project.repository.PlayerRepository;
import com.project.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    @Override
    public Page<PlayerResponseDto> getTopPlayers(Pageable pageable) {
        return playerRepository.findTopPlayers(pageable)
                .map(PlayerMapper::toDto);
    }

    @Override
    public PlayerDetailDto getPlayerById(Long id) {
        return playerRepository.findById(id)
                .map(PlayerMapper::toDetailDto)
                .orElseThrow(() -> new PlayerNotFoundException("Player by this ID does not exist"));
    }

    @Override
    public Page<PlayerDetailDto> searchPlayersByName(String name, Pageable pageable) {
        return playerRepository.findByNameContainingIgnoreCase(name, pageable)
                .map(PlayerMapper::toDetailDto);
    }

    @Override
    public Page<PlayerResponseDto> getPlayersByClusterRole(Integer clusterId, Pageable pageable) {
        return playerRepository.findByStatsClusterId(clusterId, pageable)
                .map(PlayerMapper::toDto);
    }
}