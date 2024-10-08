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
echo Rigenerazione del DB wcpdbprod

mysqladmin $force --user=root --password=myprodpwd drop wcpdbprod

# devo usare mysql e non mysqladmin perche' il secondo non mi setta il charset

mysql -u root --password=myprodpwd --host=$hostname <<SQLCOMMAND 
create database wcpdbprod character set utf8mb4;
CREATE USER 'wcpuserprod'@'localhost' IDENTIFIED BY 'myprodpwd';
GRANT ALL ON wcpdbprod.* TO 'wcpuserprod'@'localhost';
FLUSH PRIVILEGES;
SQLCOMMAND

mysql -u root --password=myprodpwd --host=$hostname wcpdbprod < wcp.sql

echo Done.
