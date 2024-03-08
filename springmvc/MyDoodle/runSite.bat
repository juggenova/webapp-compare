@REM Starts the tomcat server
@REM Connect to the site with http://localhost:8080/
@REM 
@echo off
cd /d %~dp0
gradlew -Penv=dev run --console=plain
