package easylink.dto;

// fields in list devices page
public class DeviceListDto {
    private String name;
    int id;
    String description;
    String location;
    String city;
    String groupName;
    String deviceToken;
    String tags;

    public DeviceListDto(int id, String name, String description, String location, String city, String groupName, String deviceToken, String tags) {
        this.id  = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.city = city;
        this.groupName = groupName;
        this.deviceToken = deviceToken;
        this.tags = tags;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
