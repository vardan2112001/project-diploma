import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {searchPlayers} from "../api/playersApi.jsx";

function Search() {

    const navigate = useNavigate();

    const [query, setQuery] = useState("");
    const [players, setPlayers] = useState([]);
    const [loading, setLoading] = useState(false);
    const [total, setTotal] = useState(0);

    const fetchPlayers = async () => {
        if (!query.trim()) {
            setPlayers([]);
            setTotal(0);
            return;
        }

        try {
            setLoading(true);

            const res = await searchPlayers(query, 0, 20);
            const data = res.data;

            if (Array.isArray(data)) {
                setPlayers(data);
                setTotal(data.length);
            } else {
                setPlayers(data.content || []);
                setTotal(data.totalElements || 0);
            }

        } catch (e) {
            console.error(e);
            setPlayers([]);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        const timeout = setTimeout(() => {
            fetchPlayers();
        }, 400);

        return () => clearTimeout(timeout);
    }, [query]);

    // helpers
    const getPositionShort = (position) => {
        switch (position) {
            case "Forward": return "FW";
            case "Midfielder": return "MF";
            case "Defender": return "DF";
            case "Goalkeeper": return "GK";
            default: return position;
        }
    };

    const getScoreColor = (score) => {
        if (score >= 8.5) return "var(--accent-green)";
        if (score >= 7) return "var(--accent-blue)";
        return "var(--accent-amber)";
    };

    const getInitials = (name) => {
        return name
            .split(" ")
            .map(n => n[0])
            .slice(0, 2)
            .join("")
            .toUpperCase();
    };

    // highlight search text
    const highlight = (text) => {
        if (!query) return text;

        const regex = new RegExp(`(${query})`, "gi");
        const parts = text.split(regex);

        return parts.map((part, i) =>
            part.toLowerCase() === query.toLowerCase() ? (
                <span
                    key={i}
                    style={{
                        background: "rgba(245,158,11,.2)",
                        borderRadius: "2px",
                        padding: "1px 2px"
                    }}
                >
                    {part}
                </span>
            ) : part
        );
    };

    return (
        <div className="search">

            <h1 style={{fontSize: "24px", fontWeight: 800, marginBottom: "20px"}}>
                Search Players
            </h1>

            <div
                className="search-bar"
                style={{
                    maxWidth: "100%",
                    marginBottom: "24px",
                    padding: "14px 20px",
                }}
            >
                <span style={{fontSize: "22px"}}>🔍</span>

                <input
                    type="text"
                    placeholder="Type a player name..."
                    value={query}
                    onChange={(e) => setQuery(e.target.value)}
                    style={{fontSize: "16px"}}
                />

                <span
                    style={{
                        fontSize: "12px",
                        color: "var(--text-muted)",
                        background: "var(--bg-elevated)",
                        padding: "4px 8px",
                        borderRadius: "4px",
                    }}
                >
                    ⌘K
                </span>
            </div>

            {query && !loading && (
                <p style={{fontSize: "13px", color: "var(--text-muted)", marginBottom: "16px"}}>
                    {total} results for "{query}"
                </p>
            )}

            <div style={{display: "flex", flexDirection: "column", gap: "12px"}}>

                {loading ? (
                    <p style={{color: "var(--text-muted)"}}>Loading...</p>
                ) : players.length > 0 ? (

                    players.map((player) => {
                        const pos = getPositionShort(player.position);

                        return (
                            <div
                                key={player.id}
                                onClick={() => navigate(`/players/${player.id}`)}
                                style={{
                                    display: "flex",
                                    alignItems: "center",
                                    gap: "16px",
                                    background: "var(--bg-card)",
                                    border: "1px solid var(--border)",
                                    borderRadius: "var(--radius-md)",
                                    padding: "16px 20px",
                                    cursor: "pointer",
                                    transition: ".15s",
                                }}
                                onMouseOver={(e) =>
                                    (e.currentTarget.style.borderColor = "var(--border-light)")
                                }
                                onMouseOut={(e) =>
                                    (e.currentTarget.style.borderColor = "var(--border)")
                                }
                            >
                                <div
                                    style={{
                                        width: "48px",
                                        height: "48px",
                                        borderRadius: "50%",
                                        background: "var(--gradient-hero)",
                                        display: "flex",
                                        alignItems: "center",
                                        justifyContent: "center",
                                        fontWeight: 700,
                                    }}
                                >
                                    {getInitials(player.name)}
                                </div>

                                <div style={{flex: 1}}>
                                    <div style={{fontWeight: 600}}>
                                        {highlight(player.name)}
                                    </div>

                                    <div style={{fontSize: "13px", color: "var(--text-muted)"}}>
                                        {player.club} · {pos} · {player.age} yrs
                                    </div>
                                </div>

                                <div style={{textAlign: "right"}}>
                                    <div style={{
                                        fontSize: "20px",
                                        fontWeight: 800,
                                        color: getScoreColor(player.performanceScore)
                                    }}>
                                        {player.performanceScore?.toFixed(2)}
                                    </div>
                                    <div style={{fontSize: "11px", color: "var(--text-muted)"}}>
                                        Score
                                    </div>
                                </div>

                                <span style={{color: "var(--text-muted)"}}>→</span>
                            </div>
                        );
                    })

                ) : query && !loading ? (

                    <div
                        style={{
                            marginTop: "16px",
                            padding: "32px",
                            border: "1px dashed var(--border)",
                            borderRadius: "var(--radius-md)",
                            textAlign: "center",
                        }}
                    >
                        <p style={{color: "var(--text-muted)"}}>
                            No players found for "{query}"
                        </p>
                    </div>

                ) : null}
            </div>
        </div>
    );
}

export default Search;
