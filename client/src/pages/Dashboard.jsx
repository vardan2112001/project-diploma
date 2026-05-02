import {getDashboard} from "../api/playersApi";
import {useEffect, useState} from "react";
import ClusterDistribution from "../components/ClusterDistribution.jsx";
import TopPlayersByScore from "../components/TopPlayersByScore.jsx";
import RecentTopPerformers from "../components/RecentTopPerformers.jsx";


function Dashboard() {

    const [dashboardInfo, setDashboardInfo] = useState({});

    useEffect(() => {
        const fetchDashboard = async () => {
            try {
                const res = await getDashboard();
                setDashboardInfo(res.data);
            } catch (error) {
                console.error(error);
            }
        };

        fetchDashboard();
    }, []);

    return (
        <div className="dashboard">
            <div className="flex-between mb-24">
                <div>
                    <h1 style={{ fontSize: "24px", fontWeight: 800 }}>
                        Dashboard
                    </h1>
                    <p style={{ fontSize: "14px", color: "var(--text-muted)" }}>
                        Player performance overview
                    </p>
                </div>
            </div>

            <div className="stat-cards">
                <div className="stat-card">
                    <div className="label">Total Players</div>
                    <div className="value">{dashboardInfo.totalPlayers}</div>
                    <div className="change up">From CSV import</div>
                </div>

                <div className="stat-card">
                    <div className="label">Avg Performance</div>
                    <div className="value" style={{ color: "var(--accent-blue)" }}>
                        {dashboardInfo.avgPerformance}
                    </div>
                    <div className="change" style={{ color: "var(--text-muted)" }}>
                        Across all positions
                    </div>
                </div>

                <div className="stat-card">
                    <div className="label">Clusters</div>
                    <div className="value" style={{ color: "var(--accent-purple)" }}>
                        4
                    </div>
                    <div className="change" style={{ color: "var(--text-muted)" }}>
                        K-Means groups
                    </div>
                </div>

                <div className="stat-card">
                    <div className="label">Top Score</div>
                    <div className="value" style={{ color: "var(--accent-green)" }}>
                        {dashboardInfo.topScore}
                    </div>
                    <div className="change up">▲ Best performer</div>
                </div>
            </div>

            <div
                style={{
                    display: "grid",
                    gridTemplateColumns: "1fr 1fr",
                    gap: "16px",
                    marginBottom: "24px",
                }}
            >

                <TopPlayersByScore />

                <ClusterDistribution />

            </div>

            <RecentTopPerformers />
        </div>
    );
}

export default Dashboard;
