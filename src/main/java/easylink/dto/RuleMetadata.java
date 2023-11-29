package easylink.dto;

import java.util.ArrayList;
import java.util.List;

public class RuleMetadata {
    List<Integer> deviceTypes = new ArrayList<>();

    public RuleMetadata() {}

    public List<Integer> getDeviceTypes() {
        return deviceTypes;
    }

    public void setDeviceTypes(List<Integer> deviceTypes) {
        this.deviceTypes = deviceTypes;
    }
}
