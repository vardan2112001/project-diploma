package com.project.controller;

import com.project.dto.PlayerDetailDto;
import com.project.dto.PlayerResponseDto;
import com.project.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/players")
@RequiredArgsConstructor
public class PlayerController {

    private static final int DEFAULT_PAGE_SIZE = 5;

    private final PlayerService playerService;

    @GetMapping("/top")
    public Page<PlayerResponseDto> getTopPlayers(@PageableDefault(size = 5) Pageable pageable) {
        return playerService.getTopPlayers(pageable);
    }

    @GetMapping("/{id}")
    public PlayerDetailDto getPlayerById(@PathVariable Long id) {
        return playerService.getPlayerById(id);
    }

    @GetMapping("/search")
    public Page<PlayerDetailDto> searchPlayers(@RequestParam String name,
                                               @PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable) {
        return playerService.searchPlayersByName(name, pageable);
    }

    @GetMapping("/role/{clusterId}")
    public Page<PlayerResponseDto> getPlayersByRole(@PathVariable Integer clusterId,
                                                    @PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable) {
        return playerService.getPlayersByClusterRole(clusterId, pageable);
    }
}
