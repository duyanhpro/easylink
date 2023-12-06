#!/bin/bash

# Copy file to mgmt01
scp easylink.jar mgmt01:/opt/docker/easylink/libs/
scp conf_production/application.properties mgmt01:/opt/docker/easylink/conf/
scp conf_production/logback-spring.xml mgmt01:/opt/docker/easylink/conf/

# Copy file to Mgmt02
scp easylink.jar mgmt02:/opt/docker/easylink/libs/
scp conf_production/application.properties mgmt02:/opt/docker/easylink/conf/
scp conf_production/logback-spring.xml mgmt02:/opt/docker/easylink/conf/

# Copy file to Mgmt03
scp easylink.jar mgmt03:/opt/docker/easylink/libs/
scp conf_production/application.properties mgmt03:/opt/docker/easylink/conf/
scp conf_production/logback-spring.xml mgmt03:/opt/docker/easylink/conf/

# Restart EasyLink App 
ansible mgmt -m shell -a "docker restart easylink"
