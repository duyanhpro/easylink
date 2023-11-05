package easylink.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class DeviceConnectionMonitor {

    Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    DeviceService deviceService;

    @Async
    public void runDeviceMonitorCheck() {
        log.info("Start async thread to monitor device connections");
        // load active devices

        // iterate through them and check last event_time

    }
}
