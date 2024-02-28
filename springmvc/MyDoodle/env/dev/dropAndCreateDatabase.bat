@echo off
echo Database regeneration: all data will be lost!
echo (mysql.exe must be in the system PATH)

mysqladmin --user=root --host=localhost drop wcpdbdev
mysqladmin --user=root --host=localhost refresh

mysql.exe -u root -h localhost -e "create database wcpdbdev character set utf8mb4;"

mysql.exe -u root -h localhost wcpdbdev < C:\work\gits\jugg\webapp-compare-project\webapp-compare\springmvc\MyDoodle\schema\wcp.sql
REM Uncomment the following if some custom sql is needed to create the tables
REM mysql.exe -u root -h localhost wcpdbdev < C:\work\gits\jugg\webapp-compare-project\webapp-compare\springmvc\MyDoodle\schema\wcpextra.sql

echo Done.
 
 
