#!/bin/bash

# Starts the tomcat server
# Connect to the site with http://localhost:8080/

# Change to the script's directory
cd "$(dirname "$0")"

# Create directories
mkdir -p /srv/wcpdev/tmp

# Run gradlew with the specified options
./gradlew -Penv=dev run --console=plain