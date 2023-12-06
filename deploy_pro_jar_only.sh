#!/bin/bash

# Copy file to mgmt01
scp easylink.jar mgmt01:/opt/docker/easylink/libs/

# Copy file to Mgmt02
scp easylink.jar mgmt02:/opt/docker/easylink/libs/

# Copy file to Mgmt03
scp easylink.jar mgmt03:/opt/docker/easylink/libs/

# Restart EasyLink App
ansible mgmt -m shell -a "docker restart easylink"

