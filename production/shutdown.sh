ps -ef | grep charging-station.jar | grep -v "grep" | awk '{print $2}' | xargs kill -9 
