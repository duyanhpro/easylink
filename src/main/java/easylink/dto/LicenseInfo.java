package easylink.dto;

import java.util.Date;

public class LicenseInfo {
    Boolean valid = false;
    String company;
    Integer maxDevice;
    Date expireDate;

    public LicenseInfo() {
    }

    public LicenseInfo(Boolean status, String company, Integer maxDevice, Date expireDate) {
        this.valid = status;
        this.company = company;
        this.maxDevice = maxDevice;
        this.expireDate = expireDate;
    }

    public Boolean isValid() {
        return valid;
    }
    public Boolean getValid() {
        return valid;
    }
    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Integer getMaxDevice() {
        return maxDevice;
    }

    public void setMaxDevice(Integer maxDevice) {
        this.maxDevice = maxDevice;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }
}
