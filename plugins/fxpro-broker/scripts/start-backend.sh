#!/bin/bash
# Unified Startup Script for GenX_FX Backend

echo "=== Starting FXPRO Backend (GenX_FX) ==="

# Check for Python
if ! command -v python3 &> /dev/null; then
    echo "Error: python3 is not installed."
else
    # Use the project base path as a stable location
    BACKEND_DIR="genx_fx_backend"

    if [ ! -d "$BACKEND_DIR" ]; then
        echo "Backend directory not found. Cloning GenX_FX..."
        git clone https://github.com/ejuu53899-maker/GenX_FX.git "$BACKEND_DIR"
    fi

    if [ -d "$BACKEND_DIR" ]; then
        cd "$BACKEND_DIR"

        # Set up virtual environment
        if [ ! -d "venv" ]; then
            echo "Creating virtual environment..."
            python3 -m venv venv
        fi

        if [ -f "venv/bin/activate" ]; then
            source venv/bin/activate

            # Install dependencies
            echo "Verifying dependencies..."
            pip install -r requirements.txt

            # Start the application
            echo "Launching GenX_FX main application..."
            # Use nohup or similar to prevent blocking the IDE if needed,
            # but for debugging logs, we might keep it in front if run from a terminal
            python3 src/main.py
        else
            echo "Error: Could not activate virtual environment."
        fi
    else
        echo "Error: Backend directory could not be prepared."
    fi
fi
