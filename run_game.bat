@echo off
REM BACKSTABBERZ.2 Game Runner
REM This script compiles and runs the game server with 2 client instances

setlocal enabledelayedexpansion

set "PROJECT_ROOT=c:\Users\dudet\Downloads\BACKSTABBERZ.2"
set "SRC_DIR=%PROJECT_ROOT%\src\main\java"
set "OUT_DIR=%PROJECT_ROOT%\bin"
set "MAIN_PACKAGE=me.taff_s.game"

cls
echo ========================================
echo   BACKSTABBERZ.2 - Game Launcher
echo ========================================
echo.

REM Check if bin directory exists
if not exist "%OUT_DIR%" (
    echo Creating output directory...
    mkdir "%OUT_DIR%"
)

REM Compile the project
echo Compiling project...
cd /d "%PROJECT_ROOT%"
javac -d "%OUT_DIR%" -sourcepath "%SRC_DIR%" "%SRC_DIR%\me\taff_s\game\core\*.java" "%SRC_DIR%\me\taff_s\game\player\*.java" "%SRC_DIR%\me\taff_s\game\combat\*.java" "%SRC_DIR%\me\taff_s\game\enemies\*.java" "%SRC_DIR%\me\taff_s\game\enemies\types\*.java" "%SRC_DIR%\me\taff_s\game\items\*.java" "%SRC_DIR%\me\taff_s\game\items\weapons\*.java" "%SRC_DIR%\me\taff_s\game\items\armour\*.java" "%SRC_DIR%\me\taff_s\game\items\potions\*.java" "%SRC_DIR%\me\taff_s\game\items\charms\*.java" "%SRC_DIR%\me\taff_s\game\net\*.java" "%SRC_DIR%\me\taff_s\game\world\*.java" "%SRC_DIR%\me\taff_s\game\style\*.java"

if errorlevel 1 (
    echo.
    echo Compilation failed!
    pause
    exit /b 1
)

echo Compilation successful!
echo.

REM Start the server
echo Starting Game Server...
start "BACKSTABBERZ.2 - Server" /d "%OUT_DIR%" cmd /k java %MAIN_PACKAGE%.core.GameServer

REM Wait for server to start
timeout /t 2 /nobreak

REM Start client 1
echo Starting Client 1...
start "BACKSTABBERZ.2 - Client 1" /d "%OUT_DIR%" cmd /k java %MAIN_PACKAGE%.core.GameClient

REM Start client 2
echo Starting Client 2...
start "BACKSTABBERZ.2 - Client 2" /d "%OUT_DIR%" cmd /k java %MAIN_PACKAGE%.core.GameClient

echo.
echo ========================================
echo   All processes started!
echo ========================================
echo.
echo You can now play the game!
echo.
pause
