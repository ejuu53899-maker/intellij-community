#!/bin/bash
# Cleanup script for FXPRO Backend

echo "=== Cleaning up FXPRO Backend environment ==="

BACKEND_DIR="all_in_one_backend"
LEGACY_DIR="genx_fx_backend"

for dir in "$BACKEND_DIR" "$LEGACY_DIR"; do
    if [ -d "$dir" ]; then
        echo "Removing $dir..."
        rm -rf "$dir"
    fi
done

echo "Cleanup complete."
