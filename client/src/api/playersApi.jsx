// =============================================================
// playersApi.jsx — fixed version
//
// PROBLEM IN ORIGINAL:
//   const API_URL = "http://localhost:8080/api"
//   This hardcoded URL breaks in Docker because the frontend
//   container cannot reach "localhost:8080" — that's the HOST
//   machine, not the backend container.
//
// FIX:
//   Use import.meta.env.VITE_API_URL with a fallback.
//   - In Docker production: VITE_API_URL=/api (nginx proxies it)
//   - In local dev:         VITE_API_URL=http://localhost:8080/api
//
// HOW TO SET FOR LOCAL DEV:
//   Create client/.env.local:
//     VITE_API_URL=http://localhost:8080/api
// =============================================================

import axios from "axios";

const API_URL = import.meta.env.VITE_API_URL ?? "http://localhost:8080/api";

export const getTopPlayers = (size) => {
    return axios.get(`${API_URL}/players/top`, {
        params: { size }
    });
};

export const getAllPlayers = (page = 0, size = 20) => {
    return axios.get(`${API_URL}/players`, {
        params: { page, size },
    });
};

export const getPlayersByPosition = (position, page = 0, size = 20) => {
    return axios.get(`${API_URL}/players/position`, {
        params: {
            position,
            page,
            size
        }
    });
};

export const getDashboard = () => {
    return axios.get(`${API_URL}/dashboard`);
};

export const getClusterCounts = () => {
    return axios.get(`${API_URL}/dashboard/clusters`);
};

export const getPlayersByCluster = (clusterId, page = 0, size = 5) => {
    return axios.get(`${API_URL}/players/role/${clusterId}?page=${page}&size=${size}`);
};

export const getTopPlayersPaginated = (page = 0, size = 20) => {
    return axios.get(`${API_URL}/players/top`, {
        params: { page, size }
    });
};

export const searchPlayers = (name, page = 0, size = 20) => {
    return axios.get(`${API_URL}/players/search`, {
        params: { name, page, size }
    });
};

export const getPlayerById = (id) => {
    return axios.get(`${API_URL}/players/${id}`);
};
