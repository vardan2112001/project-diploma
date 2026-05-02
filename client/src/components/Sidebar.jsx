import { NavLink } from "react-router-dom";

function Sidebar() {
    return (
        <div className="app-sidebar">
            <div className="sidebar-brand">
                <div className="icon">⚽</div>
                <div className="text">PlayerAnalytics</div>
            </div>

            <div className="sidebar-section">Main</div>

            <NavLink
                to="/"
                className={({ isActive }) =>
                    `sidebar-item ${isActive ? "active" : ""}`
                }
                end
            >
                ■ Dashboard
            </NavLink>

            <NavLink
                to="/players"
                className={({ isActive }) =>
                    `sidebar-item ${isActive ? "active" : ""}`
                }
            >
                ★ Top Players
            </NavLink>

            <NavLink
                to="/clusters"
                className={({ isActive }) =>
                    `sidebar-item ${isActive ? "active" : ""}`
                }
            >
                ⊞ Clusters
            </NavLink>

            <NavLink
                to="/search"
                className={({ isActive }) =>
                    `sidebar-item ${isActive ? "active" : ""}`
                }
            >
                🔍 Search
            </NavLink>
        </div>
    );
}

export default Sidebar;
