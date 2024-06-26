#!/bin/bash

SHOPPING_PID=$(ps -ef | grep next-server | awk '{print $2}')


if [ -z "$SHOPPING_PID" ];

then

    echo "SHOPPING_FRONT is not running"

else

    kill -9 $SHOPPING_PID

    echo "SHOPPING_FRONT stopped."

fi
