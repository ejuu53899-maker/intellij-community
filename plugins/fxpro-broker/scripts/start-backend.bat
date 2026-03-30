@echo off
echo === Starting All-in-One Desktop Mode Backend ===

where python >nul 2>nul
if %ERRORLEVEL% neq 0 (
    echo Error: python is not installed or not in PATH.
    pause
    exit /b 1
)

set BACKEND_DIR=all_in_one_backend

if not exist "%BACKEND_DIR%" (
    echo Backend directory not found. Cloning from Codeberg...
    git clone https://codeberg.org/LengKundee/all-in-one-desktop-mode-.git "%BACKEND_DIR%"
)

if exist "%BACKEND_DIR%" (
    cd "%BACKEND_DIR%"

    if not exist "venv" (
        echo Creating virtual environment...
        python -m venv venv
    )

    if exist "venv\Scripts\activate.bat" (
        call venv\Scripts\activate.bat
        echo Verifying dependencies...
        if exist "requirements.txt" (
            pip install -r requirements.txt
        )
        echo Launching API main application...
        if exist "api\main.py" (
            pip install uvicorn
            uvicorn api.main:app --host 0.0.0.0 --port 8000
        ) else (
            echo Error: api\main.py not found.
            pause
        )
    ) else (
        echo Error: Could not find virtual environment activation script.
        pause
    )
) else (
    echo Error: Backend directory could not be prepared.
    pause
)
