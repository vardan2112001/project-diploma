// =============================================================
// vite.config.js — updated with dev proxy and fixed manualChunks
// =============================================================

import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],

  server: {
    port: 5173,
    proxy: {
      // All /api/* requests during dev are proxied to the backend
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      }
    }
  },

  build: {
    // Generate source maps for production debugging
    // Set to false to reduce bundle size if not needed
    sourcemap: false,

    rollupOptions: {
      output: {

        manualChunks(id) {
          if (id.includes('node_modules')) {
            if (id.includes('axios')) {
              return 'http';
            }
            if (id.includes('react') || id.includes('react-dom') || id.includes('react-router-dom')) {
              return 'vendor';
            }
            return 'vendor-others';
          }
        }
      }
    }
  }
})