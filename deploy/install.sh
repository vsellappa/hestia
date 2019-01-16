#!/usr/bin/env bash

###############################################################################
##
## Installs HDP 3.1 , deploys the recommended cluster 
## Starts the services
## Creates Kafka topics
## Deploys and starts Microservice.
##
###############################################################################
set -o errexit
set -o nounset
set -o pipefail

##
## Util Functions
##

installUtils() {
    printf "##INFO: Installing Maven and Wget\n"
    yum install -y maven wget
}

installHDP() {
    printf "##INFO: Installing HDP\n"
    curl -sSL https://gist.github.com/vsellappa/cbaa2e9f82d513eb8dd6ca11531cd9e5/raw | sudo -E sh
    
    printf "##INFO: HDP INSTALLED\n"
    sleep 10
}

serviceExists() {
    SERVICE=$1
    SERVICE_STATUS=$(curl -u admin:${ambari_password} -X GET http://${AMBARI_HOST}:8080/api/v1/clusters/${CLUSTER_NAME}/services/${SERVICE} | grep '"status" : ' | grep -Po '([0-9]+)')

    if [[ "$SERVICE_STATUS" == 404 ]]; then
        echo 0
    else
        echo 1
    fi
}

getServiceStatus() {
    SERVICE=$1
    SERVICE_STATUS=$(curl -u admin:${ambari_password} -X GET http://${AMBARI_HOST}:8080/api/v1/clusters/${CLUSTER_NAME}/services/${SERVICE} | grep '"state" :' | grep -Po '([A-Z]+)')

    printf "##INFO: $SERVICE_STATUS\n"
}

waitForService() {
    # Ensure that Service is not in a transitional state
    SERVICE=$1
    SERVICE_STATUS=$(curl -u admin:${ambari_password} -X GET http://${AMBARI_HOST}:8080/api/v1/clusters/${CLUSTER_NAME}/services/${SERVICE} | grep '"state" :' | grep -Po '([A-Z]+)')
    
    sleep 2
    printf "##INFO: $SERVICE STATUS: $SERVICE_STATUS\n"
    LOOPESCAPE="false"
    
    if ! [[ "$SERVICE_STATUS" == STARTED || "$SERVICE_STATUS" == INSTALLED ]]; then until [ "$LOOPESCAPE" == true ]; 
    do
        SERVICE_STATUS=$(curl -u admin:${ambari_password} -X GET http://${AMBARI_HOST}:8080/api/v1/clusters/${CLUSTER_NAME}/services/${SERVICE} | grep '"state" :' | grep -Po '([A-Z]+)')
        if [[ "$SERVICE_STATUS" == STARTED || "$SERVICE_STATUS" == INSTALLED ]]; then
            LOOPESCAPE="true"
        fi
        printf "##INFO: $SERVICE Status: $SERVICE_STATUS\n"
        sleep 2
    done
    fi
}

waitForServiceToStart() {
    # Ensure that Service is not in a transitional state
    SERVICE=$1
    SERVICE_STATUS=$(curl -u admin:${ambari_password} -X GET http://${AMBARI_HOST}:8080/api/v1/clusters/${CLUSTER_NAME}/services/${SERVICE} | grep '"state" :' | grep -Po '([A-Z]+)')
    sleep 2
    printf "##INFO: $SERVICE STATUS: $SERVICE_STATUS"
    LOOPESCAPE="false"
    
    if ! [[ "$SERVICE_STATUS" == STARTED ]]; then until [ "$LOOPESCAPE" == true ]; 
    do
        SERVICE_STATUS=$(curl -u admin:${ambari_password} -X GET http://${AMBARI_HOST}:8080/api/v1/clusters/${CLUSTER_NAME}/services/${SERVICE} | grep '"state" :' | grep -Po '([A-Z]+)')
        if [[ "$SERVICE_STATUS" == STARTED ]]; then
            LOOPESCAPE="true"
        fi
        printf "##INFO: $SERVICE Status: $SERVICE_STATUS\n"
        sleep 2
    done
    fi
}

stopService() {
       	SERVICE=$1
       	SERVICE_STATUS=$(getServiceStatus $SERVICE)

       	printf "##INFO: Stopping Service $SERVICE \n"
       	if [ "$SERVICE_STATUS" == STARTED ]; then
        TASKID=$(curl -u admin:${ambari_password} -H "X-Requested-By:ambari" -i -X PUT -d '{"RequestInfo": {"context": "Stop $SERVICE"}, "ServiceInfo": {"maintenance_state" : "OFF", "state": "INSTALLED"}}' http://$AMBARI_HOST:8080/api/v1/clusters/$CLUSTER_NAME/services/$SERVICE | grep "id" | grep -Po '([0-9]+)')

        printf "##INFO: Stop $SERVICE TaskID $TASKID \n"
        sleep 2
        LOOPESCAPE="false"
        until [ "$LOOPESCAPE" == true ]; do
            TASKSTATUS=$(curl -u admin:${ambari_password} -X GET http://$AMBARI_HOST:8080/api/v1/clusters/$CLUSTER_NAME/requests/$TASKID | grep "request_status" | grep -Po '([A-Z]+)')
            if [ "$TASKSTATUS" == COMPLETED ]; then
                LOOPESCAPE="true"
            fi
            printf "##INFO: Stop $SERVICE Task Status $TASKSTATUS \n"
            sleep 2
        done
            printf "##INFO: $SERVICE Service Stopped \n"
       	elif [ "$SERVICE_STATUS" == INSTALLED ]; then
       	    printf "##INFO: $SERVICE Service Stopped \n"
       	fi
}

startService (){
    SERVICE=$1
    SERVICE_STATUS=$(getServiceStatus $SERVICE)
    
    printf "##INFO: Starting Service $SERVICE ..."
    if [ "$SERVICE_STATUS" == INSTALLED ]; then
        TASKID=$(curl -u admin:${ambari_password} -H "X-Requested-By:ambari" -i -X PUT -d '{"RequestInfo": {"context": "Start $SERVICE" }, "ServiceInfo": {"maintenance_state" : "OFF", "state": "STARTED"}}' http://${AMBARI_HOST}:8080/api/v1/clusters/$CLUSTER_NAME/services/$SERVICE | grep "id" | grep -Po '([0-9]+)')

        printf "##INFO: Start $SERVICE TaskID $TASKID"
        sleep 2
        LOOPESCAPE="false"
        until [ "$LOOPESCAPE" == true ]; do
            TASKSTATUS=$(curl -u admin:${ambari_password} -X GET http://${AMBARI_HOST}:8080/api/v1/clusters/${CLUSTER_NAME}/requests/${TASKID} | grep "request_status" | grep -Po '([A-Z]+)')
            if [ "$TASKSTATUS" == COMPLETED ]; then
                LOOPESCAPE="true"
            fi
            printf "##INFO: Start $SERVICE Task Status $TASKSTATUS\n"
            sleep 2
        done
        printf "##INFO: $SERVICE Service Started...\n"
    elif [ "$SERVICE_STATUS" == STARTED ]; then
        printf "##INFO: $SERVICE Service Started...\n"
    fi
}

startKafka() {
    KAFKA_STATUS=$(getServiceStatus KAFKA)
    
    printf "##INFO: Checking KAFKA status...\n"
    if ! [[ ${KAFKA_STATUS} == STARTED || ${KAFKA_STATUS} == INSTALLED ]]; then
        printf "##INFO: KAFKA is in a transitional state, waiting...\n"
        waitForService KAFKA
        printf "##INFO: KAFKA has entered a ready state...\n"
    fi

    if [[ $KAFKA_STATUS == INSTALLED ]]; then
        startService KAFKA
    else
        printf "##INFO: KAFKA Service Started...\n"
    fi
}

createKafkaTopics() {
    printf "##INFO: Creating Kafka Topics...\n"

    /usr/hdp/current/kafka-broker/bin/kafka-topics.sh --create --zookeeper ${AMBARI_HOST}:2181 --replication-factor 1 --partitions 1 --topic Lookup
    /usr/hdp/current/kafka-broker/bin/kafka-topics.sh --create --zookeeper ${AMBARI_HOST}:2181 --replication-factor 1 --partitions 1 --topic TxnData
    /usr/hdp/current/kafka-broker/bin/kafka-topics.sh --create --zookeeper ${AMBARI_HOST}:2181 --replication-factor 1 --partitions 1 --topic USHousePrices
    
    printf "##INFO: Created Kafka Topics...\n"
}

getMetadataFile() {
    FILENAME=$1
    URL_BASE="https://s3.eu-central-1.amazonaws.com/vshestia"
    WGET_OPTS="-q --no-check-certificate --no-proxy"

	printf "##INFO: Getting metadatafile from location: ${URL_BASE}\n"
    rm -rf /tmp/${FILENAME}

    if ! wget ${WGET_OPTS} "${URL_BASE}/${FILENAME}" -P /tmp/; then
        printf "##ERROR: Unable to get File: ${FILENAME}\n"
        exit 1
    fi
}

###############################################################################
##
## Main Program
##
###############################################################################

export AMBARI_HOST=$(hostname -f)
export ambari_password=${ambari_password:-BadPass#1}

printf "##INFO: Ambari Host: ${AMBARI_HOST}\n"

export CLUSTER_NAME=$(curl -u admin:${ambari_password} -X GET http://${AMBARI_HOST}:8080/api/v1/clusters |grep cluster_name|grep -Po ': "(.+)'|grep -Po '[a-zA-Z0-9\-_!?.]+')

if [[ -z ${CLUSTER_NAME} ]]; then
    printf "##ERROR: Could not connect to Ambari Server. Please run the install script on the same host where Ambari Server is installed.\n"
    exit 1
else
    printf "##INFO: Cluster Name: $CLUSTER_NAME\n"
fi

export ROOT_PATH=$(pwd)
printf "##INFO: Root Path: $ROOT_PATH\n"

## start Kafka in case not started
startKafka

sleep 10

## create kafka topics
createKafkaTopics

printf "##Info: Cloning Repo\n"

cd ${ROOT_PATH}
git clone https://github.com/vsellappa/hestia.git

## maven and wget installation
installUtils

FILENAME="ZILLOW_metadata.csv.zip"
getMetadataFile ${FILENAME}
unzip -o /tmp/${FILENAME} -d ${ROOT_PATH}/hestia/src/main/resources/data

## compile it
chmod 777 hestia
cd hestia
printf "##Info: Compiling hestia"

mvn clean package

## run
java -jar target/hestia-0.1-SNAPSHOT-jar-with-dependencies.jar --bootstrap.servers=${AMBARI_HOST}:6667 --file.location=/data/sampleMetadata.csv