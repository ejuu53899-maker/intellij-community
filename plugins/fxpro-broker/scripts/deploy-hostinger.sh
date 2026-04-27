#!/bin/bash
# Hostinger VPS Deployment Script for All-in-One Desktop Mode
# Tailored for Ubuntu/Debian VPS

set -e

echo "=== Deploying to VPS Jakarta 03 ==="

# 1. Update System
echo "[1/6] Updating system packages..."
sudo apt update && sudo apt upgrade -y

# 2. Install Docker
if ! command -v docker &> /dev/null; then
    echo "[2/6] Installing Docker..."
    curl -fsSL https://get.docker.com -o get-docker.sh
    sudo sh get-docker.sh
    sudo usermod -aG docker $USER
else
    echo "[2/6] Docker already installed."
fi

# 3. Install Docker Compose
if ! command -v docker-compose &> /dev/null; then
    echo "[3/6] Installing Docker Compose..."
    sudo apt install -y docker-compose
else
    echo "[3/6] Docker Compose already installed."
fi

# 4. Configure Firewall
echo "[4/6] Configuring UFW firewall..."
sudo ufw allow 22/tcp
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp
sudo ufw allow 8000/tcp
sudo ufw --force enable

# 5. Clone and Prepare Project
PROJECT_DIR="/opt/all-in-one-desktop"
echo "[5/6] Preparing project at $PROJECT_DIR..."
sudo mkdir -p $PROJECT_DIR
sudo chown $USER:$USER $PROJECT_DIR
cd $PROJECT_DIR

if [ ! -d ".git" ]; then
    git clone https://codeberg.org/LengKundee/all-in-one-desktop-mode-.git .
else
    git pull origin main
fi

# 6. Launch Services
echo "[6/6] Launching Docker services..."
if [ -f "docker-compose.yml" ]; then
    docker-compose up -d
    echo "Services started successfully."
else
    echo "Warning: docker-compose.yml not found. Starting API directly..."
    # Fallback or manual start instructions
fi

echo "=== Deployment Complete ==="
echo "Access your dashboard at: http://your-vps-ip:8000"
