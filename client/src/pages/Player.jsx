import {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import {getPlayerById} from "../api/playersApi.jsx";

function Player() {

    const { id } = useParams();
    const navigate = useNavigate();

    const [player, setPlayer] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchPlayer = async () => {
            try {
                setLoading(true);

                const res = await getPlayerById(id);

                console.log("PLAYER API RESPONSE:", res.data);

                setPlayer(res.data);

            } catch (e) {
                console.error(e);
            } finally {
                setLoading(false);
            }
        };

        fetchPlayer();
    }, [id]);

    const getInitials = (name = "") =>
        name.split(" ").map(n => n[0]).slice(0, 2).join("").toUpperCase();

    const getClusterLabel = (clusterId) => {
        switch (clusterId) {
            case 0: return "Anchors";
            case 1: return "Game Changers";
            case 2: return "Guardians";
            case 3: return "Playmakers";
            default: return "Unknown";
        }
    };

    const isGoalkeeper = player?.position === "Goalkeeper";

    if (loading) {
        return <div style={{padding: 20, color: "var(--text-muted)"}}>Loading...</div>;
    }

    if (!player) {
        return <div style={{padding: 20}}>Player not found</div>;
    }

    return (
        <div className="player">

            <div style={{marginBottom: "20px"}}>
                <button
                    className="btn btn-ghost"
                    style={{fontSize: "13px"}}
                    onClick={() => navigate("/players")}
                >
                    ← Back to Players
                </button>
            </div>

            <div className="player-profile">

                <div className="profile-sidebar">

                    <div className="profile-avatar">
                        {getInitials(player.name)}
                    </div>

                    <div className="profile-name">
                        {player.name}
                    </div>

                    <div className="profile-club">
                        {player.club} · {player.position}
                    </div>

                    <div className="profile-meta">

                        <div className="item">
                            <div className="val">{player.age ?? "-"}</div>
                            <div className="lbl">Age</div>
                        </div>

                        <div className="item">
                            <div className="val">{player.appearances ?? "-"}</div>
                            <div className="lbl">Apps</div>
                        </div>

                        <div className="item">
                            <div className="val" style={{color: `var(--cluster-${player.clusterId})`}}>
                                {getClusterLabel(player.clusterId)}
                            </div>
                            <div className="lbl">Cluster</div>
                        </div>
                    </div>

                    <div className="profile-score">
                        <div className="heading">Performance Score</div>
                        <div className="big">
                            {player.performanceScore?.toFixed(2)}
                        </div>
                    </div>
                </div>

                <div>

                    <div className="stats-grid">

                        <div className="stat-block">

                            <div className="title">
                                {isGoalkeeper ? "Goalkeeper Stats" : "Offensive"}
                            </div>

                            {!isGoalkeeper && (
                                <>
                                    <div className="stat-row">
                                        <span className="label">Goals</span>
                                        <span className="val">{player.goals ?? 0}</span>
                                    </div>

                                    <div className="stat-row">
                                        <span className="label">Assists</span>
                                        <span className="val">{player.assists ?? 0}</span>
                                    </div>

                                    <div className="stat-row">
                                        <span className="label">Shots on Target</span>
                                        <span className="val">{player.shotsOnTarget ?? 0}</span>
                                    </div>
                                </>
                            )}

                            {isGoalkeeper && (
                                <>
                                    <div className="stat-row">
                                        <span className="label">Saves</span>
                                        <span className="val">{player.saves ?? 0}</span>
                                    </div>

                                    <div className="stat-row">
                                        <span className="label">Clean Sheets</span>
                                        <span className="val">{player.cleanSheets ?? 0}</span>
                                    </div>
                                </>
                            )}
                        </div>

                        <div className="stat-block">

                            <div className="title">General</div>

                            <div className="stat-row">
                                <span className="label">Appearances</span>
                                <span className="val">{player.appearances ?? 0}</span>
                            </div>

                            <div className="stat-row">
                                <span className="label">Club</span>
                                <span className="val">{player.club}</span>
                            </div>

                            <div className="stat-row">
                                <span className="label">Position</span>
                                <span className="val">{player.position}</span>
                            </div>
                        </div>

                        <div className="stat-block" style={{gridColumn: "span 2"}}>

                            <div className="title">Score Breakdown</div>

                            <div
                                style={{
                                    marginTop: "8px",
                                    padding: "16px",
                                    background: "var(--bg-primary)",
                                    borderRadius: "8px",
                                    fontSize: "13px",
                                    fontFamily: "JetBrains Mono, monospace",
                                    color: "var(--text-secondary)",
                                    lineHeight: 2,
                                }}
                            >
                                performanceScore =
                                (weighted model based on stats)
                                <br />
                                <br />
                                goals: {player.goals} <br />
                                assists: {player.assists} <br />
                                saves: {player.saves} <br />
                                shots: {player.shotsOnTarget} <br />
                                appearances: {player.appearances}
                                <br />
                                <br />
                                <span style={{color: "var(--accent-blue-light)", fontWeight: 700}}>
                                    {player.performanceScore?.toFixed(2)}
                                </span>
                            </div>
                        </div>

                    </div>

                    <div className="chart-placeholder" style={{marginTop: "16px"}}>

                        <div className="chart-title">Player Radar</div>

                        <div className="chart-subtitle">
                            Compare stats across cluster
                        </div>

                        <div style={{
                            flex: 1,
                            display: "flex",
                            alignItems: "center",
                            justifyContent: "center",
                            color: "var(--text-muted)"
                        }}>
                            Radar chart coming soon
                        </div>
                    </div>

                </div>
            </div>
        </div>
    );
}

export default Player;
