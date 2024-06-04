#!/bin/bash

SOCIAL_PID=$(ps -ef | grep next-server | awk '{print $2}')


if [ -z "$SOCIAL_PID" ];

then

    echo "SOCIAL_FRONT is not running"

else

    kill -9 $SOCIAL_PID

    echo "SOCIAL_FRONT stopped."

fi
