package com.project.service;

import com.project.dto.PlayerDetailDto;
import com.project.dto.PlayerResponseDto;

import com.project.exceptions.PlayerNotFoundException;
import com.project.mapper.PlayerMapper;
import com.project.repository.PlayerRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;


    public List<PlayerResponseDto> getTopPlayers(Pageable pageable) {
        return playerRepository.findTopPlayers(pageable)
                .stream()
                .map(PlayerMapper::toDto)
                .toList();
    }

    public PlayerDetailDto getPlayerById(Long id) {
        return playerRepository.findById(id)
                .map(PlayerMapper::toDetailDto)
                .orElseThrow(() -> new PlayerNotFoundException("Player by this ID does not exists"));
    }

    public List<PlayerDetailDto> searchPlayersByName(String name) {
        return playerRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(PlayerMapper::toDetailDto)
                .toList();
    }

    public List<PlayerResponseDto> getPlayersByClusterRole(Integer clusterId, Pageable pageable) {
        return playerRepository.findByStatsClusterId(clusterId, pageable)
                .stream()
                .map(PlayerMapper::toDto)
                .toList();
    }

}