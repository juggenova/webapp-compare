#!/bin/bash
# Script di deploy del sito
# Parametri:
# 1 - path del file war, che deve stare dentro alla directory di progetto a qualunque livello, e.g. /srv/amdprod/deploy/ldm.prod.[02-maggio-2014-17.17].war
# Va eseguito con l'utente normale, non root

warFile=$1
# tomcatBase=$2
# vhost=$3

# Dal path del war ricavo gli altri folder
tmp=/${warFile#/srv/*/}
baseDir=${warFile%$tmp}
destDir=${baseDir}/webapp
scriptDir=${baseDir}/bin

echo
echo $(date) Start of deployment script
echo warFile=$warFile
echo destDir=$destDir
#echo vhost=$vhost
echo Stopping webapp...
# Il processo è gestito da monit
sudo monit stop wcpprod
# $scriptDir/stop.sh

# Cancello la precedente app
sudo rm -rf ${destDir}/*

sudo unzip -qod ${destDir} ${warFile}
sudo chown -R tomcat8 ${destDir}

echo Starting webapp...
# Il processo è gestito da monit
sudo monit start wcpprod
# $scriptDir/start.sh
# $scriptDir/start.sh 2>> /dev/null

# Elimino il file di deploy per evitare di riempire il filesystem
rm ${warFile}

tail -20 ${baseDir}/logs/site.log

echo $(date) End of deployment script

