#!/bin/bash

JAR=shopping-0.0.1-SNAPSHOT.jar
LOG=/home/ubuntu/social/data/api/back.log

echo "BACK started."

nohup java -jar $JAR > $LOG 2>&1 &