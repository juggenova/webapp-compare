#!/bin/bash

if [ "$EUID" -ne "$(id -u tomcat8)" ]; then
    echo "This script must be run as tomcat8"
    exit 1
fi

acroenv=wcpprod

curl -s telnet://localhost:9088 <<< "wcpproddown"

# Identification string for the process
ident='/srv/wcpprod/webapp/WEB-INF/classes'

# Wait for the process to exit
myPid=$( ps -ww -C java -o pid,args | grep $ident | awk '{print $1}' )

if [ "$myPid" != "" ]; then
        COUNTER=0
        while [  $COUNTER -lt 10 ]; do
                process=$( ps -h $myPid )
                if [ "$process" = "" ]; then
                        COUNTER=99;
                else
                        echo Waiting for process to stop...
                        sleep 1
                        let COUNTER=COUNTER+1
                fi
        done

        if [ "$COUNTER" != "99" ]; then
                echo "!!!! Can't stop server !!!!"
		echo "killing process"
		kill -9 $myPid
                exit 0
        fi
fi


