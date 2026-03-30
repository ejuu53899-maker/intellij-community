@echo off
echo === Starting FXPRO Backend (GenX_FX) ===

where python >nul 2>nul
if %ERRORLEVEL% neq 0 (
    echo Error: python is not installed or not in PATH.
    pause
    exit /b 1
)

set BACKEND_DIR=genx_fx_backend

if not exist "%BACKEND_DIR%" (
    echo Backend directory not found. Cloning GenX_FX...
    git clone https://github.com/ejuu53899-maker/GenX_FX.git "%BACKEND_DIR%"
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
        pip install -r requirements.txt
        echo Launching GenX_FX main application...
        python src\main.py
    ) else (
        echo Error: Could not find virtual environment activation script.
        pause
    )
) else (
    echo Error: Backend directory could not be prepared.
    pause
)
