# Dockerfile
# Step 1: Build the React app
FROM node:20 AS build

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .
RUN npm run build

# Step 2: Serve with a static server
FROM nginx:alpine

COPY --from=build /app/dist /usr/share/nginx/html

# Optional: Add a custom nginx config for SPA routing
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
