package com.project.controller;

import com.project.dto.PlayerDetailDto;
import com.project.dto.PlayerResponseDto;
import com.project.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerServiceImpl;

    @GetMapping("/top")
    public List<PlayerResponseDto> getTopPlayers(@PageableDefault Pageable pageable) {
        return playerServiceImpl.getTopPlayers(pageable);
    }

    @GetMapping("/{id}")
    public PlayerDetailDto getPlayerById(@PathVariable Long id) {
        return playerServiceImpl.getPlayerById(id);
    }

    @GetMapping("/search")
    public List<PlayerDetailDto> searchPlayers(@RequestParam String name) {
        return playerServiceImpl.searchPlayersByName(name);
    }

    @GetMapping("/role/{clusterId}")
    public List<PlayerResponseDto> getPlayersByRole(
            @PathVariable Integer clusterId,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return playerServiceImpl.getPlayersByClusterRole(clusterId, pageable);
    }
}