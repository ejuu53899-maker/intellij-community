#!/bin/bash
# End-to-End Startup Script managed by Jules

set -e
echo "Starting Full Setup for GenX FX Trading & Git Build..."

# 1. Start GenX Backend
echo "[1/3] Starting GenX Backend Bridge..."
if [ ! -d "all_in_one_backend" ]; then
    echo "❌ Error: all_in_one_backend directory not found."
    exit 1
fi

cd all_in_one_backend
if [ ! -d "venv" ]; then
    python3 -m venv venv
fi
source venv/bin/activate
pip install pandas fastapi uvicorn redis pydantic-settings > /dev/null 2>&1
export TESTING=true
export PYTHONPATH=$PYTHONPATH:.
# Kill existing backend if running
kill $(lsof -t -i :8000 2>/dev/null) 2>/dev/null || true
uvicorn api.main:app --host 0.0.0.0 --port 8000 > ../backend.log 2>&1 &
echo "✅ Backend started in background (log: backend.log)"
cd ..

# 2. Build mouy-git
echo "[2/3] Building Git from source (mouy-git)..."
if [ ! -d "mouy-git" ]; then
    echo "Cloning target Git repository..."
    git clone https://github.com/Mouy-leng172/git.git mouy-git
fi
cd mouy-git
# Ensure build dependencies are met (basic check)
make -j$(nproc) > ../git_build.log 2>&1 || { echo "❌ Build failed. Check git_build.log"; exit 1; }
./git --version
echo "✅ Git build complete."
cd ..

# 3. Environment Kickstart
echo "[3/3] Running Intelligence IDX Kickstart..."
if [ -f "./intelligence-idx/kickstart.sh" ]; then
    bash ./intelligence-idx/kickstart.sh
else
    echo "⚠️ Warning: kickstart.sh not found."
fi

echo "🚀 Full setup completed successfully by Jules."
