import {useEffect, useState} from "react";
import {getTopPlayers} from "../api/playersApi.jsx";

function TopPlayersByScore() {

    const [topPlayersByScore, setTopPlayersByScore] = useState([]);

    useEffect(() => {
        const fetchTopPlayersByScore = async () => {
            try {
                const res = await getTopPlayers(8);
                setTopPlayersByScore(res.data.content);
            } catch (error) {
                console.error(error);
            }
        };

        fetchTopPlayersByScore();
    }, []);

    return (
        <div className="chart-placeholder">
            <div className="chart-title">Top Players by Score</div>
            <div className="chart-subtitle">
                Performance scores (min. 10 appearances)
            </div>

            <div className="chart-area">
                {topPlayersByScore.map((player) => (
                    <div className="chart-bar-group" key={player.name}>
                        <div
                            className="chart-bar"
                            style={{
                                height: `${player.performanceScore * 10}%`,
                                background:
                                    player.performanceScore > 8.5
                                        ? "var(--accent-blue)"
                                        : player.performanceScore > 8.0
                                            ? "var(--accent-cyan)"
                                            : "var(--accent-purple)"
                            }}
                        />
                        <div className="chart-bar-label">
                            {player.name.slice(0, 3).toUpperCase()}
                        </div>
                    </div>
                ))}
            </div>
        </div>
    )

}

export default TopPlayersByScore;
