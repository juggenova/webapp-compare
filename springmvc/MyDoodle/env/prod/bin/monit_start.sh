#!/bin/bash

if [ "$EUID" -ne "$(id -u tomcat8)" ]; then
    echo "This script must be run as tomcat8"
    exit 1
fi

acroenv=wcpprod

webappDir="/srv/$acroenv/webapp"
binDir="/srv/$acroenv/bin"
pidsDir="/srv/$acroenv/pids"
classpath="$webappDir/WEB-INF/classes:$webappDir/WEB-INF/lib/*"

mkdir -p $pidsDir

cd $webappDir
echo Web application starting in $webappDir
/srv/javas/jdk17/bin/java -Dfile.encoding=utf-8 -classpath $classpath net.yadaframework.core.YadaTomcatServer $acroenv $webappDir $webappDir &

# Save the PID of the background process to the pid file
echo $! > $pidsDir/wcpprod.pid



