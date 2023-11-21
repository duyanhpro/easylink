package easylink.service;

import easylink.dto.LicenseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.vivas.core.util.EncryptUtil;
import vn.vivas.core.util.JsonUtil;

@Service
public class LicenseService {

    static final String LICENSE = "LICENSE";
    public static final String ek = "easylink@2023";
    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ConfigParamService configService;
    @Autowired
    DeviceService deviceService;

    public LicenseInfo getLicense() throws Exception {
        String licString = getLicenseKey();
        if (licString == null || licString.isEmpty())
            return null;
        // decrypt licString
        String lic2 = EncryptUtil.decryptTripleDES(ek, licString);
        //System.out.println(lic2);
        // parse json to object
        return JsonUtil.parse(lic2, LicenseInfo.class);
    }

    public String getLicenseKey() {
        Object a = configService.getConfig(LICENSE);
        if (a!=null)
            return a.toString();
        return "";
    }
    public boolean updateLicenseKey(String licenseKey) {
        if (licenseKey==null || licenseKey.isEmpty()) return false;
        try {
            String json = EncryptUtil.decryptTripleDES(ek, licenseKey);
            LicenseInfo a = JsonUtil.parse(json, LicenseInfo.class);
            configService.updateConfig(LICENSE, licenseKey, String.class.getName());
            return true;
        } catch (Exception e) {
            log.info("License key invalid: ", e.getMessage());
        }
        return false;
    }
    public boolean validateLicenseKey(String licenseKey) {
        if (licenseKey==null || licenseKey.isEmpty()) return false;
        try {
            String json = EncryptUtil.decryptTripleDES(ek, licenseKey);
            LicenseInfo a = JsonUtil.parse(json, LicenseInfo.class);
            return true;
        } catch (Exception e) {
            log.info("License key invalid: ", e.getMessage());
        }
        return false;
    }

    public boolean checkLicenseAddDevice() {
        LicenseInfo l = null;
        try {
            l = getLicense();
            if (l == null) return false;
            if (l.getExpireDate() != null) {
                if (l.getExpireDate().getTime() < System.currentTimeMillis()) return false;
            }
            if (l.getMaxDevice() <= deviceService.countDevice())
                return false;
        } catch (Exception e) {
            log.error("License check exception", e);
            return false;
        }
        return true;
    }

}
