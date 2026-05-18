package com.project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "player_stats")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerStats {

    @Id
    private Long playerId;

    private Integer appearances;
    private Integer goals;
    private Integer assists;

    @Column(name = "shots_on_target")
    private Integer shotsOnTarget;

    @Column(name = "clean_sheets")
    private Integer cleanSheets;

    @Column(name = "saves")
    private Integer saves;

    @Column(name = "performance_score")
    private Double performanceScore;

    @Column(name = "wins")
    private Integer wins;

    @Column(name = "cluster_id")
    private Integer clusterId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "player_id")
    private Player player;
}
