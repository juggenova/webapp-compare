mysql.exe --verbose -u root -h localhost -e "create database wcpdbdev character set utf8mb4;"
mysql.exe --verbose -u root -h localhost -e "CREATE USER 'wcpuserdev'@'localhost' IDENTIFIED BY 'mydevpwd'; GRANT ALL ON wcpdbdev.* TO 'wcpuserdev'@'localhost'; FLUSH PRIVILEGES;"

pause
