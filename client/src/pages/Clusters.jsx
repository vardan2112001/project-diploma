import {useEffect, useState} from "react";
import {getClusterCounts, getPlayersByCluster} from "../api/playersApi.jsx";

function Clusters() {

    const [clusters, setClusters] = useState([]);
    const [selectedCluster, setSelectedCluster] = useState(0);

    const [players, setPlayers] = useState([]);
    const [page, setPage] = useState(0);

    const [totalPages, setTotalPages] = useState(1);
    const [totalElements, setTotalElements] = useState(0);

    const pageSize = 10;

    const CLUSTER_NAMES = {
        0: "Anchors",
        1: "Game Changers",
        2: "Guardians",
        3: "Playmakers"
    };

    const CLUSTER_DESCRIPTIONS = {
        0: "Defensive specialists",
        1: "Impact players & leaders",
        2: "Goalkeepers & defenders",
        3: "Creative midfielders & assisters"
    };

    // 🔥 POSITION SHORT (как в TopPlayers)
    const getPositionShort = (position) => {
        switch (position) {
            case "Forward": return "FW";
            case "Midfielder": return "MF";
            case "Defender": return "DF";
            case "Goalkeeper": return "GK";
            default: return position;
        }
    };

    // 🔹 Кластеры
    useEffect(() => {
        const fetchCounts = async () => {
            try {
                const res = await getClusterCounts();
                const data = res.data;

                setClusters(data.map(([id, count]) => ({
                    id,
                    count
                })));
            } catch (e) {
                console.error(e);
            }
        };

        fetchCounts();
    }, []);

    // 🔹 Игроки
    useEffect(() => {
        const fetchPlayers = async () => {
            try {
                const res = await getPlayersByCluster(selectedCluster, page, pageSize);
                const data = res.data;

                setPlayers(data.content || []);
                setTotalPages(data.totalPages || 1);
                setTotalElements(data.totalElements || 0);

            } catch (e) {
                console.error(e);
                setPlayers([]);
            }
        };

        fetchPlayers();
    }, [selectedCluster, page]);

    return (
        <div className="clusters">

            <h1 style={{ fontSize: "24px", fontWeight: 800, marginBottom: "8px" }}>
                Player Clusters
            </h1>

            <p style={{
                color: "var(--text-muted)",
                fontSize: "14px",
                marginBottom: "24px",
            }}>
                K-Means grouping based on stats
            </p>

            {/* CLUSTERS */}
            <div className="cluster-grid">
                {clusters.map(cluster => (
                    <div
                        key={cluster.id}
                        className={`cluster-card ${selectedCluster === cluster.id ? "active" : ""}`}
                        onClick={() => {
                            setSelectedCluster(cluster.id);
                            setPage(0);
                        }}
                        style={{cursor: "pointer"}}
                    >
                        <div className="number">{cluster.count}</div>
                        <div className="role">{CLUSTER_NAMES[cluster.id]}</div>
                        <div className="count">
                            Cluster {cluster.id} · {CLUSTER_DESCRIPTIONS[cluster.id]}
                        </div>
                    </div>
                ))}
            </div>

            {/* TABLE */}
            <div className="data-table-wrap">

                <div className="table-header">
                    <h3>
                        Cluster {selectedCluster} — {CLUSTER_NAMES[selectedCluster]}
                    </h3>

                    <div style={{ fontSize: "13px", color: "var(--text-muted)" }}>
                        {totalElements} players
                    </div>
                </div>

                <table>
                    <thead>
                    <tr>
                        <th>Player</th>
                        <th>Club</th>
                        <th>Position</th>
                        <th>Score</th>
                    </tr>
                    </thead>

                    <tbody>
                    {players.length > 0 ? (
                        players.map(player => {
                            const pos = getPositionShort(player.position);

                            return (
                                <tr key={player.id}>
                                    <td style={{ fontWeight: 600 }}>{player.name}</td>
                                    <td style={{ color: "var(--text-secondary)" }}>{player.club}</td>

                                    {/* 🔥 FIXED BADGE COLORS */}
                                    <td>
                                        <span className={`badge badge-${pos.toLowerCase()}`}>
                                            {pos}
                                        </span>
                                    </td>

                                    <td style={{ fontWeight: 600 }}>
                                        {player.performanceScore?.toFixed(2)}
                                    </td>
                                </tr>
                            );
                        })
                    ) : (
                        <tr>
                            <td colSpan="4">No players found</td>
                        </tr>
                    )}
                    </tbody>
                </table>

                {/* PAGINATION */}
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

export default Clusters;
