#!/bin/bash
# Intelligence IDX - Intelligent Production Script
# Streamlines building and packaging for production release.

set -e

echo "🏗️ Starting Intelligence IDX Production Build..."

# 1. Ensure clean state (optional but recommended for production)
# echo "🧹 Cleaning build artifacts..."
# ./bazel.cmd clean

# 2. Build MCP Server Plugin
echo "🔨 Building intellij-community MCP server plugin..."
# We use the standard installer script for current OS as indicated in README.md
if [ -f "./installers.cmd" ]; then
    ./installers.cmd -Dintellij.build.target.os=current
    echo "✅ Production build complete. Artifacts are in the 'out' directory."
else
    echo "❌ installers.cmd not found. Cannot perform production build."
    exit 1
fi

echo "🚀 Intelligent Production process finished successfully."
