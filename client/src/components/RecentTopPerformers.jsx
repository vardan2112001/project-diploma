import {useEffect, useState} from "react";
import {getTopPlayers} from "../api/playersApi.jsx";
import {useNavigate} from "react-router-dom";

function RecentTopPerformers() {

    const [topTopPlayers, setTopTopPlayers] = useState([]);

    const navigate = useNavigate();

    const CLUSTER_NAMES = {
        0: "Anchors",
        1: "Game Changers",
        2: "Guardians",
        3: "Playmakers"
    };

    const CLUSTER_COLORS = {
        0: "--cluster-0",
        1: "--cluster-1",
        2: "--cluster-2",
        3: "--cluster-3"
    };

    useEffect(() => {
        const fetchTopPlayers = async () => {
            try {
                const res = await getTopPlayers(5);
                setTopTopPlayers(res.data.content || []);
            } catch (error) {
                console.error(error);
                setTopTopPlayers([]);
            }
        };

        fetchTopPlayers();
    }, []);

    const getPositionShort = (position) => {
        switch (position) {
            case "Forward": return "FW";
            case "Midfielder": return "MF";
            case "Defender": return "DF";
            case "Goalkeeper": return "GK";
            default: return position;
        }
    };

    const getCluster = (clusterId) => {
        return {
            color: CLUSTER_COLORS[clusterId] || "--cluster-0",
            label: CLUSTER_NAMES[clusterId] || "Other"
        };
    };

    const getWidth = (score) => {
        return `${(score / 10) * 100}%`;
    };

    return (
        <div className="data-table-wrap">
            <div className="table-header">
                <h3>Recent Top Performers</h3>
                <button
                    className="btn btn-outline"
                    style={{fontSize: "12px"}}
                    onClick={() => navigate("/players")}
                >
                    View All →
                </button>
            </div>

            <table>
                <thead>
                <tr>
                    <th>Player</th>
                    <th>Club</th>
                    <th>Position</th>
                    <th>Score</th>
                    <th>Cluster</th>
                </tr>
                </thead>

                <tbody>
                {topTopPlayers.length === 0 ? (
                    <tr>
                        <td colSpan="5" style={{textAlign: "center"}}>
                            No data
                        </td>
                    </tr>
                ) : (
                    topTopPlayers.map((player) => {
                        const pos = getPositionShort(player.position);
                        const cluster = getCluster(player.clusterId ?? 0);
                        const width = getWidth(player.performanceScore);

                        return (
                            <tr key={player.id}>
                                <td style={{fontWeight: 600}}>
                                    {player.name}
                                </td>

                                <td style={{color: "var(--text-secondary)"}}>
                                    {player.club}
                                </td>

                                <td>
                                    <span className={`badge badge-${pos.toLowerCase()}`}>
                                        {pos}
                                    </span>
                                </td>

                                <td>
                                    <div className="score-bar">
                                        <div className="bar">
                                            <div
                                                className="fill"
                                                style={{
                                                    width: width,
                                                    background: "var(--accent-amber)"
                                                }}
                                            />
                                        </div>

                                        <span style={{fontSize: "13px", fontWeight: 600}}>
                                            {player.performanceScore?.toFixed(2)}
                                        </span>
                                    </div>
                                </td>

                                <td>
                                    <span
                                        className="cluster-dot"
                                        style={{background: `var(${cluster.color})`}}
                                    />
                                    {cluster.label}
                                </td>
                            </tr>
                        );
                    })
                )}
                </tbody>
            </table>
        </div>
    );
}

export default RecentTopPerformers;
