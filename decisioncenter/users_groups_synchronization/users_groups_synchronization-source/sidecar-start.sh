#!/bin/bash

while true
do
   echo "synchronize groups and users every minute"
   curl -X 'POST' 'http://localhost:9060/decisioncenter-api/v1/repository/users-roles-registry?eraseAllUsersAndGroups=true' -H 'accept: */*'   -H 'Content-Type: multipart/form-data'   -F 'file=@/tmp/group-security-configurations.xml;type=text/xml' -u odmAdmin:odmAdmin
   echo "Sleep 60 seconds"
   sleep 60
done
