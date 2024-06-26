#!/bin/bash


LOG=/home/ubuntu/shopping/frontend/shopping.log

echo "SHOPPING_FRONT started."
 
nohup npm run start > $LOG 2>&1 &
