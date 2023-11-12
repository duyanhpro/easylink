package easylink.dto;

import easylink.entity.Device;
import easylink.entity.Group;

import java.util.List;

public class DeviceGroupDto {
    Device device;
    Group group;

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
