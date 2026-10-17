@echo off
setlocal
if "%~1"=="" (
    echo Usage: run-scenario.bat ^<scenario^>
    exit /b 2
)

pushd "%~dp0.."
call mvn -q -DskipTests compile
if errorlevel 1 goto :failed

if not exist "%~dp0build" mkdir "%~dp0build"
javac -encoding UTF-8 -cp "target\classes" -d "%~dp0build" "%~dp0ScenarioRunner.java"
if errorlevel 1 goto :failed

java -cp "%~dp0build;target\classes" scenarios.ScenarioRunner "%~1"
set "result=%errorlevel%"
popd
exit /b %result%

:failed
set "result=%errorlevel%"
popd
exit /b %result%
