@echo off
echo ========================================
echo Tunisian Agricultural Export AI System
echo ========================================
echo.

echo 1. Cleaning project...
call mvn clean -q

echo 2. Compiling project...
call mvn compile -q
if %errorlevel% neq 0 (
    echo ERROR: Compilation failed!
    pause
    exit /b 1
)

echo.
echo 3. Choose what to run:
echo    1. Test FAO Data Transformation
echo    2. Run Main Application
echo    3. Run Dashboard with FAO Data
echo    4. Run Simple Dashboard
echo    5. Test Data Pipeline
echo.

set /p choice="Enter choice (1-5): "

if "%choice%"=="1" (
    echo Running FAO Data Test...
    call mvn exec:java -Dexec.mainClass="tn.isg.economics.TestFAOData"
) else if "%choice%"=="2" (
    echo Running Main Application...
    call mvn exec:java -Dexec.mainClass="tn.isg.economics.Main"
) else if "%choice%"=="3" (
    echo Running Dashboard with FAO Data...
    call mvn exec:java -Dexec.mainClass="tn.isg.economics.dashboard.DashboardMain"
) else if "%choice%"=="4" (
    echo Running Simple Dashboard...
    call mvn exec:java -Dexec.mainClass="tn.isg.economics.dashboard.AgriculturalDashboard"
) else if "%choice%"=="5" (
    echo Testing Data Pipeline...
    call mvn exec:java -Dexec.mainClass="tn.isg.economics.DataPipelineTest"
) else (
    echo Invalid choice
)

pause
