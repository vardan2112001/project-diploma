package com.project.repository;

import com.project.entity.Player;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    @Query("SELECT p FROM Player p  WHERE  p.stats.appearances >10 ORDER BY p.stats.performanceScore DESC")
    List<Player> findTopPlayers(Pageable pageable);

    List<Player> findByNameContainingIgnoreCase(String name);

    boolean existsByIdIsNotNull();

    @Query("SELECT p FROM Player p JOIN FETCH p.stats WHERE p.stats IS NOT NULL")
    List<Player> findAllPlayersWithStats();

    @Query("SELECT p FROM Player p JOIN FETCH p.stats s WHERE s.clusterId = :clusterId")
    List<Player> findByStatsClusterId(@Param("clusterId") Integer clusterId, Pageable pageable);

    @Query("SELECT p FROM Player p JOIN p.stats s WHERE s.appearances > :minAppearances AND p.position = 'Forward'")
    List<Player> findForwards(@Param("minAppearances") Integer minAppearances);

    @Query("SELECT p FROM Player p JOIN p.stats s WHERE s.appearances > :minAppearances AND p.position = 'Defender'")
    List<Player> findDefenders(@Param("minAppearances") Integer minAppearances);

    @Query("SELECT p FROM Player p JOIN p.stats s WHERE s.appearances > :minAppearances AND p.position = 'Midfielder'")
    List<Player> findMidfielders(@Param("minAppearances") Integer minAppearances);

    @Query("SELECT p FROM Player p JOIN p.stats s WHERE s.appearances > :minAppearances AND p.position = 'Goalkeeper'")
    List<Player> findGoalkeepers(@Param("minAppearances") Integer  minAppearances);

}