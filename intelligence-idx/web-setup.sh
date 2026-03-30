#!/bin/bash
# Intelligence IDX - Web Technology Setup Script
# Automates the initialization of modern web projects (Vite, Next.js, Prisma).

set -e

TECH_STACK=${1:-"vite"}
PROJECT_NAME=${2:-"web-app"}

echo "⚡ Starting Intelligence IDX Web Setup for '$PROJECT_NAME' using $TECH_STACK..."

# Check for Node.js
if ! command -v node &> /dev/null; then
    echo "❌ Node.js is required. Please run './intelligence-idx/kickstart.sh' first."
    exit 1
fi

case $TECH_STACK in
  "vite")
    echo "📦 Initializing Vite project..."
    npm create vite@latest "$PROJECT_NAME" -- --template vanilla-ts
    cd "$PROJECT_NAME" && npm install
    ;;
  "nextjs")
    echo "📦 Initializing Next.js project..."
    npx create-next-app@latest "$PROJECT_NAME" --typescript --tailwind --eslint
    ;;
  "prisma")
    if [ ! -d "$PROJECT_NAME" ]; then
        echo "❌ Project directory '$PROJECT_NAME' not found. Create a project first."
        exit 1
    fi
    echo "📦 Adding Prisma to '$PROJECT_NAME'..."
    cd "$PROJECT_NAME"
    npm install prisma --save-dev
    npx prisma init
    ;;
  *)
    echo "❌ Unknown tech stack: $TECH_STACK. Supported: vite, nextjs, prisma"
    exit 1
    ;;
esac

echo "✨ Web setup for $TECH_STACK complete!"
