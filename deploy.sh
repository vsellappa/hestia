#!/usr/bin/env bash

###############################################################################
##
## Deployment script
##
###############################################################################
set -o errexit
set -o nounset
set -o pipefail

## check if maven is installed


## Name the services required
SERVICES="(KAFKA, DRUID, SUPERSET)"

export kafka_service="localhost:9092"


## Check if the services are running (use default ports)

## Check for the TOPICS

## Create the TOPICS if they do not exist
