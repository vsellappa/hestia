#!/usr/bin/env bash

## Launch Centos/RHEL 7 VM with at least 8 vcpu / 32Gb+ memory / 100Gb disk

###############################################################################
##
## Installs HDP
##
## hdp version : hdp_ver
## deployed services : ambari_services
## auto-starts deployed services
##
###############################################################################

set -o errexit
set -o nounset
set -o pipefail

##
## Util Functions
##

rmRepo() {
    #remove unneeded repos for some AMIs
    if [[ -f /etc/yum.repos.d/zfs.repo ]]; then
        rm -f /etc/yum.repos.d/zfs.repo
    fi

    if [[ -f /etc/yum.repos.d/lustre.repo ]]; then
        rm -f /etc/yum.repos.d/lustre.repo
    fi
}

installUtils() {
    printf "##INFO: Installing pre-requisites\n"
    sudo yum localinstall -y https://dev.mysql.com/get/mysql57-community-release-el7-8.noarch.rpm
    sudo yum install -y git python-argparse epel-release mysql-connector-java* mysql-community-server nc
}

dbForDruidAndSuperset() {
    printf "##INFO: Setting up Database\n"

    sudo systemctl enable mysqld.service
    sudo systemctl start mysqld.service

    #extract system generated Mysql password
    oldpass=$( grep 'temporary.*root@localhost' /var/log/mysqld.log | tail -n 1 | sed 's/.*root@localhost: //' )

    #create sql file that
        # 1. reset Mysql password to temp value and create druid/superset/registry/streamline schemas and users
        # 2. sets passwords for druid/superset users to ${db_password}

    cat << EOF > mysql-setup.sql
        ALTER USER 'root'@'localhost' IDENTIFIED BY 'Secur1ty!';
        uninstall plugin validate_password;
        CREATE DATABASE druid DEFAULT CHARACTER SET utf8;
        CREATE DATABASE superset DEFAULT CHARACTER SET utf8;
        CREATE USER 'druid'@'%' IDENTIFIED BY '${db_password}';
        CREATE USER 'superset'@'%' IDENTIFIED BY '${db_password}';
        GRANT ALL PRIVILEGES ON *.* TO 'druid'@'%' WITH GRANT OPTION;
        GRANT ALL PRIVILEGES ON *.* TO 'superset'@'%' WITH GRANT OPTION;
        commit;
EOF

    #execute sql file
    mysql -h localhost -u root -p"$oldpass" --connect-expired-password < mysql-setup.sql

    #change Mysql password to ${db_password}
    mysqladmin -u root -p'Secur1ty!' password ${db_password}

    #test password and confirm dbs created
    mysql -u root -p${db_password} -e 'show databases;'

    printf "##INFO: MySQL DB Setup complete\n"
}

getConfigurationJson() {
    printf "##INFO: Creating blueprint for deployment\n"
    curl -sSL ${bp_template} > configuration-custom.json
}

###############################################################################
##
## Main Loop
##
###############################################################################

##
## export variables with defaults
##
export cluster_name=${cluster_name:-hdp}
export ambari_password=${ambari_password:-BadPass#1}
export db_password=${db_password:-StrongPassword}
export host_count=${host_count:-1}
export ambari_services=${ambari_services:-HDFS MAPREDUCE2 YARN ZOOKEEPER KAFKA DRUID SUPERSET}
export hdp_ver=${hdp_ver:-3.1}
export ambari_version=2.7.3.0
export bp_template="https://gist.github.com/vsellappa/ba89bde690b972ec08dd24cafad614e4/raw"

printf "##INFO: Starting install of HDP $hdp_ver\n"

cd ~
git clone https://github.com/seanorama/ambari-bootstrap.git

## removing unwanted repos
rmRepo

## installing prereqs
installUtils

## setting up db for druid and superset
dbForDruidAndSuperset

printf "##INFO: Installing Ambari\n"

export install_ambari_server=true
curl -sSL https://raw.githubusercontent.com/seanorama/ambari-bootstrap/master/ambari-bootstrap.sh | sudo -E sh

printf "##INFO: Waiting 30s for Ambari to come up\n"
sleep 30

printf "##INFO: Changing Ambari password\n"
curl -iv -u admin:admin -H "X-Requested-By: ambari" -X PUT -d "{ \"Users\": { \"user_name\": \"admin\", \"old_password\": \"admin\", \"password\": \"${ambari_password}\" }}" http://localhost:8080/api/v1/users/admin

sudo ambari-server setup --jdbc-db=mysql --jdbc-driver=/usr/share/java/mysql-connector-java.jar

printf "##INFO: Setting recommendation strategy\n"
export ambari_stack_version=${hdp_ver}
export recommendation_strategy="ALWAYS_APPLY_DONT_OVERRIDE_CUSTOM_VALUES"

cd ~/ambari-bootstrap/deploy

## get blueprint template
getConfigurationJson

printf "##INFO: Deploying HDP"
./deploy-recommended-cluster.bash