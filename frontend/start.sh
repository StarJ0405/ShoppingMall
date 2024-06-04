#!/bin/bash


LOG=/home/ubuntu/social/frontend/social.log


nohup npm run start > $LOG 2>&1 &
