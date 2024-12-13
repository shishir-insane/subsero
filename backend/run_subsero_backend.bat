@echo off
echo Starting SubSero Application...

:: Start Backend
echo Starting Backend...
for /f "delims=" %%i in (.env) do set %%i
start cmd /k " mvn spring-boot:run"
:: mvn clean spring-boot:run

echo Backend is running in separate terminal...
pause
