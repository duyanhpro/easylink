package easylink;

import easylink.dto.LicenseInfo;
import easylink.service.LicenseService;
import vn.vivas.core.util.DateUtil;
import vn.vivas.core.util.EncryptUtil;
import vn.vivas.core.util.JsonUtil;

public class TestLicense {
    public static void main(String args[]) throws Exception {
//        LicenseInfo a = new LicenseInfo(true,"Cục kỹ thuật - Binh chủng Thông tin Liên lạc",
//                3, DateUtil.newDate("20/11/2022", "dd/MM/yyyy"));
        LicenseInfo a = new LicenseInfo(true,"Cục kỹ thuật - Binh chủng Thông tin Liên lạc",
                3, null);
        String key = EncryptUtil.encryptTripleDES(LicenseService.ek, JsonUtil.object2string(a));
        System.out.println(key);
    }
}
