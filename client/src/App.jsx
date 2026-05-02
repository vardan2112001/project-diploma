import './index.css';
import { Routes, Route } from "react-router-dom";
import Sidebar from "./components/Sidebar.jsx";
import Dashboard from "./pages/Dashboard.jsx";
import TopPlayers from "./pages/TopPlayers.jsx";
import Clusters from "./pages/Clusters.jsx";
import Search from "./pages/Search.jsx";
import Player from "./pages/Player.jsx";

function App() {

  return (
      <>
        <div style={{ maxWidth: "1340px", margin: "0 auto", padding: "0 32px" }}>
          <div className="mockup-frame">

            <div className="frame-body">
              <div className="app-layout">

                <Sidebar />

                <div className="app-main">

                  <Routes>
                    <Route path="/" element={<Dashboard />} />
                    <Route path="/players" element={<TopPlayers />} />
                    <Route path="/players/:id" element={<Player />} />
                    <Route path="/clusters" element={<Clusters />} />
                    <Route path="/search" element={<Search />} />
                  </Routes>

                </div>
              </div>
            </div>
          </div>
        </div>
      </>
  )
}

export default App
