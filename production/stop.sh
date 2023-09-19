ps -ef | grep easylink.jar | grep -v "grep" | awk '{print $2}' | xargs kill -9
