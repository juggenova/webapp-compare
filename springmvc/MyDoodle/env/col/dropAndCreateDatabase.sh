#!/bin/bash
# Use the "force" parameter to skip prompts
# Note for yada developers: the $ in the yada template is the escaped form of the dollar sign

hostname=localhost
force=

if [ "$1" == "force" ]
then
	force=-f
fi

echo 
echo Rigenerazione del DB wcpdbcol

mysqladmin $force --user=root --password=null drop wcpdbcol

# devo usare mysql e non mysqladmin perche' il secondo non mi setta il charset

mysql -u root --password=null --host=$hostname <<SQLCOMMAND 
create database wcpdbcol character set utf8mb4;
CREATE USER 'wcpusercol'@'localhost' IDENTIFIED BY 'null';
GRANT ALL ON wcpdbcol.* TO 'wcpusercol'@'localhost';
FLUSH PRIVILEGES;
SQLCOMMAND

mysql -u root --password=null --host=$hostname wcpdbcol < wcp.sql
mysql -u root --password=null --host=$hostname wcpdbcol < wcpextra.sql

echo Done.
