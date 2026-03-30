#!/bin/bash
# Intelligence IDX - Vite Setup Script
# Automates the creation of a Vite-based project within the Intelligence IDX toolset.

set -e

PROJECT_NAME=${1:-"my-vite-app"}

echo "⚡ Starting Intelligence IDX Vite Setup for '$PROJECT_NAME'..."

# Check for Node.js
if ! command -v node &> /dev/null; then
    echo "❌ Node.js is required for Vite. Please run './intelligence-idx/kickstart.sh' first."
    exit 1
fi

# Initialize Vite project
echo "📦 Initializing Vite project..."
npm create vite@latest "$PROJECT_NAME" -- --template vanilla-ts

cd "$PROJECT_NAME"
npm install

echo "✨ Vite setup complete! You can start the dev server with:"
echo "   cd $PROJECT_NAME && npm run dev"
