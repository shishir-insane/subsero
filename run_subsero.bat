@echo off
echo Starting SubSero Application...

:: Start Backend
echo Starting Backend...
start cmd /k "cd backend && gradlew bootRun"

:: Start Frontend
echo Starting Frontend...
start cmd /k "cd frontend && npm run dev"

echo Both Backend and Frontend are running in separate terminals.
pause
