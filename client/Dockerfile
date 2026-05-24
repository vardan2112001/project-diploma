# =============================================================
# STAGE 1: BUILD
# Node.js image to install dependencies and build the React app
# =============================================================
FROM node:20-alpine AS builder

WORKDIR /app

# Copy package files first for dependency caching
# node_modules layer is reused unless package*.json changes
COPY package.json package-lock.json ./

# Clean install — uses package-lock.json exactly (reproducible builds)
RUN npm ci

# Copy the rest of the source code
COPY . .

# Build the production bundle to /app/dist
# VITE_API_URL is injected at build time via ARG → ENV
# This allows the nginx proxy path (/api) to be used in production
ARG VITE_API_URL=/api
ENV VITE_API_URL=${VITE_API_URL}

RUN npm run build

# =============================================================
# STAGE 2: SERVE
# Minimal nginx image to serve the static build output
# Final image is ~25MB
# =============================================================
FROM nginx:1.27-alpine AS runtime

# Remove the default nginx welcome page config
RUN rm /etc/nginx/conf.d/default.conf

# Copy our custom nginx config (handles SPA routing + API proxy)
COPY nginx.conf /etc/nginx/conf.d/app.conf

# Copy the built React app from the builder stage
COPY --from=builder /app/dist /usr/share/nginx/html

# nginx listens on port 80 inside the container
EXPOSE 80

# Healthcheck for nginx
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:80/ || exit 1

CMD ["nginx", "-g", "daemon off;"]
