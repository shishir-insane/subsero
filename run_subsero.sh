#!/bin/bash

echo "Starting SubSero Application..."

# Start Backend
echo "Starting Backend..."
export $(cat backend/.env | xargs)
gnome-terminal -- bash -c "cd backend && ./gradlew bootRun; exec bash"

# Start Frontend
echo "Starting Frontend..."
gnome-terminal -- bash -c "cd frontend && npm run dev; exec bash"

echo "Both Backend and Frontend are running in separate terminals."