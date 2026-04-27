#!/bin/bash
# Unified Startup Script for All-in-One Desktop Mode Backend (Codeberg)

echo "=== Starting All-in-One Desktop Mode Backend ==="

# Check for Python
if ! command -v python3 &> /dev/null; then
    echo "Error: python3 is not installed."
else
    # Use the project base path as a stable location
    BACKEND_DIR="all_in_one_backend"

    if [ ! -d "$BACKEND_DIR" ]; then
        echo "Backend directory not found. Cloning from Codeberg..."
        git clone https://codeberg.org/LengKundee/all-in-one-desktop-mode-.git "$BACKEND_DIR"
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
            if [ -f "requirements.txt" ]; then
                pip install -r requirements.txt
            fi

            # Start the application
            echo "Launching API main application..."
            if [ -f "api/main.py" ]; then
                # Run using uvicorn as suggested by package.json
                pip install uvicorn
                uvicorn api.main:app --host 0.0.0.0 --port 8000
            else
                echo "Error: api/main.py not found."
            fi
        else
            echo "Error: Could not activate virtual environment."
        fi
    else
        echo "Error: Backend directory could not be prepared."
    fi
fi
