#!/bin/bash
# Intelligence IDX - Kickstart Script
# Automates environment initialization for JetBrains plugin development.

set -e

echo "🚀 Starting Intelligence IDX Kickstart..."

# 1. Verify JDK 21
echo "🔍 Checking JDK 21..."
if [[ "$JAVA_HOME" == *"java-21"* ]]; then
    echo "✅ JDK 21 found at $JAVA_HOME"
else
    # Fallback check
    if java -version 2>&1 | grep -q "21"; then
        echo "✅ Java 21 is active."
    else
        echo "❌ JDK 21 not found. Please ensure JetBrains Runtime 21 is installed."
        exit 1
    fi
fi

# 2. Verify Bazel
echo "🔍 Checking Bazel..."
if command -v bazel &> /dev/null; then
    echo "✅ Bazel found: $(bazel --version)"
else
    echo "❌ Bazel not found. Please install Bazelisk or Bazel."
    exit 1
fi

# 3. Verify Node.js (for AI/Guide rendering)
echo "🔍 Checking Node.js..."
if command -v node &> /dev/null; then
    echo "✅ Node.js found: $(node --version)"
else
    echo "⚠️ Node.js not found. Guide rendering may be disabled."
fi

# 4. Initialize Plugins
echo "📦 Initializing Android plugins..."
if [ -f "./getPlugins.sh" ]; then
    ./getPlugins.sh --shallow
    echo "✅ Plugins initialized."
else
    echo "⚠️ getPlugins.sh not found. Skipping plugin initialization."
fi

echo "✨ Kickstart complete! Your environment is ready for A6-9V and MQL5 integration."
