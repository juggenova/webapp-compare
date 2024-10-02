@REM Starts the tomcat server
@REM Connect to the site with http://localhost:8080/
@REM 
@echo off
cd /d %~dp0
mkdir \srv 2> nul 
mkdir \srv\wcpdev 2> nul 
mkdir \srv\wcpdev\tmp 2> nul 

gradlew -Penv=dev run --console=plain
