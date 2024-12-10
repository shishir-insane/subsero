@echo off
echo Starting SubSero Application...

:: Start Backend
echo Starting Backend...
for /f "delims=" %%i in ("backend/.env") do set %%i
start cmd /k "cd backend && mvn spring-boot:run"

:: Start Frontend
echo Starting Frontend...
start cmd /k "cd frontend && npm run dev"

echo Both Backend and Frontend are running in separate terminals.
pause
