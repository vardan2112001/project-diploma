import {useEffect, useState} from "react";
import {getClusterCounts} from "../api/playersApi.jsx";

const CLUSTER_NAMES = {
    0: "Anchors",
    1: "Game Changers",
    2: "Guardians",
    3: "Playmakers"
};

function ClusterDistribution() {

    const [clustersCounts, setClustersCounts] = useState([]);

    useEffect(() => {
        const fetchClusters = async () => {
            try {
                const res = await getClusterCounts();
                setClustersCounts(res.data);
            } catch (error) {
                console.error(error);
            }
        };

        fetchClusters();
    }, []);

    const preparedClusters = clustersCounts.map(([id, count]) => ({
        id,
        name: CLUSTER_NAMES[id],
        count
    }));

    const total = preparedClusters.reduce((sum, c) => sum + c.count, 0);

    const clustersWithPercent = preparedClusters.map(c => ({
        ...c,
        percent: total ? (c.count / total) * 100 : 0
    }));

    const gradient = clustersWithPercent
        .reduce(
            (acc, c) => {
                const start = acc.current;
                const end = start + c.percent;

                acc.parts.push(
                    `var(--cluster-${c.id}) ${start}% ${end}%`
                );

                return {
                    current: end,
                    parts: acc.parts
                };
            },
            { current: 0, parts: [] }
        )
        .parts.join(", ");

    return (
        <div className="chart-placeholder">
            <div className="chart-title">Cluster Distribution</div>
            <div className="chart-subtitle">
                Players grouped by K-Means algorithm
            </div>

            <div
                style={{
                    flex: 1,
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                    gap: "40px",
                }}
            >
                <div
                    style={{
                        width: "160px",
                        height: "160px",
                        borderRadius: "50%",
                        background: `conic-gradient(${gradient})`,
                        position: "relative",
                    }}
                >
                    <div
                        style={{
                            position: "absolute",
                            top: "50%",
                            left: "50%",
                            transform: "translate(-50%,-50%)",
                            width: "80px",
                            height: "80px",
                            borderRadius: "50%",
                            background: "var(--bg-card)",
                        }}
                    ></div>
                </div>

                <div style={{ fontSize: "13px" }}>
                    {clustersWithPercent.map(c => (
                        <div key={c.id} style={{ marginBottom: "8px" }}>
                    <span
                        className="cluster-dot"
                        style={{ background: `var(--cluster-${c.id})` }}
                    ></span>
                            {c.name} — {c.percent.toFixed(0)}%
                        </div>
                    ))}
                </div>
            </div>
        </div>
    )
}

export default ClusterDistribution;
