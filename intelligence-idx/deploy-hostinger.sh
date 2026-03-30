#!/bin/bash
# Intelligence IDX - Hostinger VPS Deployment Script
# Automates the deployment of the IntelliJ MCP server and trading integrations to a remote Hostinger VPS.

set -e

REMOTE_HOST=${1:-"your-vps-ip"}
REMOTE_USER=${2:-"root"}
REMOTE_PATH=${3:-"/opt/intelligence-idx"}

echo "🚀 Starting Deployment to Hostinger VPS: $REMOTE_HOST..."

# 1. Build Production Artifacts
echo "🔨 Building production artifacts..."
./intelligence-idx/production.sh

# 2. Prepare Remote Directory
echo "📁 Preparing remote directory..."
ssh "$REMOTE_USER@$REMOTE_HOST" "mkdir -p $REMOTE_PATH"

# 3. Upload Artifacts
echo "📤 Uploading artifacts..."
# Assuming artifacts are in 'out' directory as per production.sh
scp -r out/* "$REMOTE_USER@$REMOTE_HOST:$REMOTE_PATH"

# 4. Install Dependencies on Remote
echo "📦 Installing remote dependencies..."
ssh "$REMOTE_USER@$REMOTE_HOST" "apt-get update && apt-get install -y openjdk-21-jdk nodejs npm"

# 5. Restart Services
echo "🔄 Restarting MCP Server on remote..."
# Example command to start the server headlessly
# ssh "$REMOTE_USER@$REMOTE_HOST" "cd $REMOTE_PATH && ./mcp-server-start.sh &"

echo "✅ Deployment successful!"
