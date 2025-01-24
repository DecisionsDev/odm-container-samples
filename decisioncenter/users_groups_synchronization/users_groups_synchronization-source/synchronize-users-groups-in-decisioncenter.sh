#!/bin/bash

while true; do
   echo "Calling /v1/repository/users-roles-registry Decision Center Endpoint..."
   curl -X 'POST' 'http://localhost:9060/decisioncenter-api/v1/repository/users-roles-registry?eraseAllUsersAndGroups=true' -H 'accept: */*'   -H 'Content-Type: multipart/form-data'   -F 'file=@./group-security-configurations.xml;type=text/xml' -u odmAdmin:odmAdmin
   echo "Sleep 30 seconds"
  sleep 30
done
