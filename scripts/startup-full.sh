#!/bin/bash
# End-to-End Startup Script managed by Jules

set -e
echo "Starting Full Setup for GenX FX Trading & Git Build..."

# 1. Start GenX Backend
echo "[1/3] Starting GenX Backend Bridge..."
cd all_in_one_backend
python3 -m venv venv
source venv/bin/activate
pip install pandas fastapi uvicorn redis pydantic-settings > /dev/null 2>&1
export TESTING=true
uvicorn api.main:app --host 0.0.0.0 --port 8000 > ../backend.log 2>&1 &
cd ..

# 2. Build mouy-git
echo "[2/3] Building Git from source (mouy-git)..."
cd mouy-git
make -j$(nproc) > ../git_build.log 2>&1
./git --version
cd ..

# 3. Environment Kickstart
echo "[3/3] Running Intelligence IDX Kickstart..."
bash ./intelligence-idx/kickstart.sh

echo "Full setup completed successfully by Jules."
