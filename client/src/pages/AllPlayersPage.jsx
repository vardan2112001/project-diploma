import { useEffect, useState, useCallback } from "react";
import { useNavigate } from "react-router-dom";
import {
    getAllPlayers,
    searchPlayers
} from "../api/playersApi.jsx";

const PAGE_SIZE = 20;

export default function AllPlayersPage() {

    const navigate = useNavigate();

    const [players, setPlayers] = useState([]);
    const [page, setPage] = useState(0);

    const [totalPages, setTotalPages] = useState(1);
    const [totalElements, setTotalElements] = useState(0);

    const [searchQuery, setSearchQuery] = useState("");

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    // ───── LOAD PLAYERS ─────
    const loadPlayers = useCallback(async () => {

        try {
            setLoading(true);
            setError(null);

            const res =
                searchQuery.trim() === ""
                    ? await getAllPlayers(page, PAGE_SIZE)
                    : await searchPlayers(
                        searchQuery.trim(),
                        page,
                        PAGE_SIZE
                    );

            const data = res.data;

            setPlayers(data?.content || []);
            setTotalPages(data?.totalPages || 1);
            setTotalElements(data?.totalElements || 0);

        } catch (err) {
            console.error(err);
            setError("Failed to load players.");
            setPlayers([]);
        } finally {
            setLoading(false);
        }

    }, [page, searchQuery]);

    useEffect(() => {
        loadPlayers();
    }, [loadPlayers]);

    // ───── SEARCH ─────
    const handleSearchChange = (e) => {
        setSearchQuery(e.target.value);
        setPage(0);
    };

    // ───── POSITION HELPERS ─────
    const getPositionShort = (position) => {

        if (!position) return "—";

        const p = position.toUpperCase();

        switch (p) {
            case "FORWARD":
            case "STRIKER":
            case "FW":
                return "FW";

            case "MIDFIELDER":
            case "MF":
                return "MF";

            case "DEFENDER":
            case "DF":
                return "DF";

            case "GOALKEEPER":
            case "GK":
                return "GK";

            default:
                return position;
        }
    };

    return (
        <div className="topPlayers">

            {/* HEADER */}
            <div className="flex-between mb-24">

                <div>

                    <h1
                        className="section-title"
                        style={{
                            marginBottom: 0,
                            fontSize: "24px"
                        }}
                    >
                        All Players
                    </h1>

                    <p
                        style={{
                            color: "var(--text-muted)",
                            fontSize: "13px",
                            marginTop: "4px"
                        }}
                    >
                        {totalElements} players total
                    </p>
                </div>

                {/* SEARCH */}
                <div className="search-bar">
                    <span className="icon">🔍</span>

                    <input
                        type="text"
                        placeholder="Search players by name..."
                        value={searchQuery}
                        onChange={handleSearchChange}
                    />
                </div>

            </div>

            {/* TABLE */}
            <div className="data-table-wrap">

                <table>

                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Player</th>
                        <th>Club</th>
                        <th>Position</th>
                        <th>Age</th>
                        <th></th>
                    </tr>
                    </thead>

                    <tbody>

                    {loading ? (
                        <tr>
                            <td
                                colSpan="6"
                                style={{ textAlign: "center" }}
                            >
                                Loading...
                            </td>
                        </tr>
                    ) : error ? (
                        <tr>
                            <td
                                colSpan="6"
                                style={{
                                    textAlign: "center",
                                    color: "var(--accent-red)"
                                }}
                            >
                                {error}
                            </td>
                        </tr>
                    ) : players.length === 0 ? (
                        <tr>
                            <td
                                colSpan="6"
                                style={{
                                    textAlign: "center"
                                }}
                            >
                                No players found
                            </td>
                        </tr>
                    ) : (
                        players.map((player, index) => {

                            const pos = getPositionShort(player.position);

                            return (
                                <tr
                                    key={player.id}
                                    style={{ cursor: "pointer" }}
                                    onClick={() =>
                                        navigate(`/players/${player.id}`)
                                    }
                                >

                                    <td style={{ fontWeight: 700 }}>
                                        {page * PAGE_SIZE + index + 1}
                                    </td>

                                    <td style={{ fontWeight: 600 }}>
                                        {player.name}
                                    </td>

                                    <td
                                        style={{
                                            color: "var(--text-secondary)"
                                        }}
                                    >
                                        {player.club ||
                                            player.clubName ||
                                            "—"}
                                    </td>

                                    <td>
                                        <span
                                            className={`badge badge-${pos.toLowerCase()}`}
                                        >
                                            {pos}
                                        </span>
                                    </td>

                                    <td>
                                        {player.age ?? "—"}
                                    </td>

                                    <td style={{
                                        display: "flex",
                                        justifyContent: "flex-end"
                                    }}>

                                        <button
                                            className="btn btn-ghost"
                                            style={{
                                                fontSize: "12px",
                                                whiteSpace: "nowrap"
                                            }}
                                            onClick={(e) => {
                                                e.stopPropagation();

                                                navigate(
                                                    `/players/${player.id}`
                                                );
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

                {/* PAGINATION */}
                {totalPages > 1 && (

                    <div className="pagination">

                        <span
                            style={{
                                fontSize: "13px",
                                color: "var(--text-muted)"
                            }}
                        >
                        Page {page + 1} of {totalPages}
                    </span>

                        <div className="page-btns">

                            <div
                                className="page-btn"
                                onClick={() =>
                                    setPage(p =>
                                        Math.max(p - 1, 0)
                                    )
                                }
                            >
                                ‹
                            </div>

                            {[...Array(totalPages).keys()]
                                .slice(
                                    Math.max(0, page - 1),
                                    page + 2
                                )
                                .map(p => (
                                    <div
                                        key={p}
                                        className={`page-btn ${
                                            p === page ? "active" : ""
                                        }`}
                                        onClick={() => setPage(p)}
                                    >
                                        {p + 1}
                                    </div>
                                ))}

                            <div
                                className="page-btn"
                                onClick={() =>
                                    setPage(p =>
                                        Math.min(
                                            p + 1,
                                            totalPages - 1
                                        )
                                    )
                                }
                            >
                                ›
                            </div>

                        </div>

                    </div>

                )}

            </div>

        </div>
    );
}
