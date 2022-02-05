#!/bin/sh

## Usage -  ./display-mongo-stats.sh mongodb-benchmark-replica-set-0

MONGO_POD_NAME=$1   # Mongo Pod Name from Open Shift Cluster

oc cp display-mongo-stats.js $MONGO_POD_NAME:/tmp
oc exec $MONGO_POD_NAME -- bash -c "/var/lib/mongodb-mms-automation/mongodb-linux-x86_64-4.4.0-ent/bin/mongo < /tmp/display-mongo-stats.js"