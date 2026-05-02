import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {getTopPlayersPaginated, getPlayersByPosition} from "../api/playersApi.jsx";

function TopPlayers() {

    const navigate = useNavigate();

    const [players, setPlayers] = useState([]);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(1);

    const [positionFilter, setPositionFilter] = useState("ALL");
    const [loading, setLoading] = useState(false);

    const pageSize = 20;

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

    // 🔹 LOAD PLAYERS (server-side filter)
    useEffect(() => {
        const fetchPlayers = async () => {
            try {
                setLoading(true);

                let res;

                if (positionFilter === "ALL") {
                    res = await getTopPlayersPaginated(page, pageSize);
                } else {
                    res = await getPlayersByPosition(positionFilter, page, pageSize);
                }

                const data = res.data;

                setPlayers(data?.content || data || []);
                setTotalPages(data?.totalPages || 1);

            } catch (e) {
                console.error(e);
                setPlayers([]);
            } finally {
                setLoading(false);
            }
        };

        fetchPlayers();
    }, [page, positionFilter]);

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

    const getWidth = (score) => `${(score / 10) * 100}%`;

    const getScoreColor = (score) => {
        if (score >= 8.5) return "var(--accent-green)";
        if (score >= 7) return "var(--accent-blue)";
        return "var(--accent-amber)";
    };

    return (
        <div className="topPlayers">

            <div className="flex-between mb-24">
                <h1 style={{fontSize: "24px", fontWeight: 800}}>
                    Top Players
                </h1>

                <div style={{display: "flex", gap: "8px"}}>
                    {["ALL", "FW", "MF", "DF", "GK"].map(btn => (
                        <button
                            key={btn}
                            className={`btn ${positionFilter === btn ? "btn-primary" : "btn-outline"}`}
                            style={{fontSize: "12px"}}
                            onClick={() => {
                                setPositionFilter(btn);
                                setPage(0);
                            }}
                        >
                            {btn}
                        </button>
                    ))}
                </div>
            </div>

            <div className="data-table-wrap">

                <table>
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Player</th>
                        <th>Club</th>
                        <th>Position</th>
                        <th>Age</th>
                        <th>Score</th>
                        <th>Cluster</th>
                        <th></th>
                    </tr>
                    </thead>

                    <tbody>
                    {loading ? (
                        <tr>
                            <td colSpan="8" style={{textAlign: "center"}}>
                                Loading...
                            </td>
                        </tr>
                    ) : players.length === 0 ? (
                        <tr>
                            <td colSpan="8" style={{textAlign: "center"}}>
                                No players found
                            </td>
                        </tr>
                    ) : (
                        players.map((player, index) => {
                            const pos = getPositionShort(player.position);
                            const cluster = getCluster(player.clusterId ?? 0);

                            return (
                                <tr
                                    key={player.id}
                                    style={{cursor: "pointer"}}
                                    onClick={() => navigate(`/players/${player.id}`)}
                                >
                                    <td style={{fontWeight: 700}}>
                                        {page * pageSize + index + 1}
                                    </td>

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

                                    <td>{player.age}</td>

                                    <td>
                                        <div className="score-bar">
                                            <div className="bar">
                                                <div
                                                    className="fill"
                                                    style={{
                                                        width: getWidth(player.performanceScore),
                                                        background: getScoreColor(player.performanceScore)
                                                    }}
                                                />
                                            </div>
                                            <span style={{fontWeight: 600}}>
                                                {player.performanceScore?.toFixed(2)}
                                            </span>
                                        </div>
                                    </td>

                                    <td style={{whiteSpace: "nowrap"}}>
                                        <span
                                            className="cluster-dot"
                                            style={{background: `var(${cluster.color})`}}
                                        />
                                        {cluster.label}
                                    </td>

                                    <td>
                                        <button
                                            className="btn btn-ghost"
                                            style={{fontSize: "12px", whiteSpace: "nowrap"}}
                                            onClick={(e) => {
                                                e.stopPropagation();
                                                navigate(`/players/${player.id}`);
                                            }}
                                        >
                                            View →
                                        </button>
                                    </td>
                                </tr>
                            );
                        })
                    )}
                    </tbody>
                </table>

                <div className="pagination">
                    <div className="info">
                        Page {page + 1} of {totalPages}
                    </div>

                    <div className="page-btns">

                        <div
                            className="page-btn"
                            onClick={() => setPage(p => Math.max(p - 1, 0))}
                        >
                            ‹
                        </div>

                        {[...Array(totalPages).keys()]
                            .slice(Math.max(0, page - 1), page + 2)
                            .map(p => (
                                <div
                                    key={p}
                                    className={`page-btn ${p === page ? "active" : ""}`}
                                    onClick={() => setPage(p)}
                                >
                                    {p + 1}
                                </div>
                            ))}

                        <div
                            className="page-btn"
                            onClick={() => setPage(p => Math.min(p + 1, totalPages - 1))}
                        >
                            ›
                        </div>

                    </div>
                </div>

            </div>
        </div>
    );
}

export default TopPlayers;
