package easylink;

import java.io.IOException;
import java.net.URISyntaxException;

//import org.assertj.core.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.thingsboard.rest.client.RestClient;
//import org.thingsboard.server.common.data.Device;
//import org.thingsboard.server.common.data.asset.Asset;
//import org.thingsboard.server.common.data.asset.AssetInfo;
//import org.thingsboard.server.common.data.device.DeviceSearchQuery;
//import org.thingsboard.server.common.data.id.EntityId;
//import org.thingsboard.server.common.data.page.PageLink;
//import org.thingsboard.server.common.data.relation.EntityRelation;
//import org.thingsboard.server.common.data.relation.EntitySearchDirection;
//import org.thingsboard.server.common.data.relation.RelationTypeGroup;
//import org.thingsboard.server.common.data.relation.RelationsSearchParameters;


public class TestMain {

	static Logger log = LoggerFactory.getLogger(TestMain.class);
	/**
	 * @param args
	 * @throws IOException 
	 * @throws URISyntaxException 
	 */
	public static void main(String[] args) throws IOException, URISyntaxException {
		log.info("Test");
		
		testThingsBoard();
		
		//String st = "{\\\"timestamp\\\": 1597901702, \\\"event_type\\\": \\\"traffic_light\\\", \\\"stream_id\\\": \\\"5e99758e9a9cccf295f60189\\\", \\\"vehicles\\\": [{\\\"vehicle_plate\\\": \\\"19TF30803\\\", \\\"vehicle_plate_capture\\\": \\\"https://iva.vivas.vn/capture/traffic/traffic_light/lp_1597901701.0663297.jpg\\\", \\\"img_capture\\\": \\\"https://iva.vivas.vn/capture/traffic/traffic_light/1597901701.0663297.jpg\\\", \\\"vehicle_type\\\": \\\"motorbike\\\", \\\"video_capture\\\": \\\"https://iva.vivas.vn/capture/traffic/videos/1597901695.mp4\\\"}, {\\\"vehicle_plate\\\": null, \\\"vehicle_plate_capture\\\": \\\"\\\", \\\"img_capture\\\": \\\"https://iva.vivas.vn/capture/traffic/traffic_light/1597901701.0713096.jpg\\\", \\\"vehicle_type\\\": \\\"motorbike\\\", \\\"video_capture\\\": \\\"https://iva.vivas.vn/capture/traffic/videos/1597901695.mp4\\\"}]}";
//		String st = FileUtil.readFile("C:/DEV/TMP/tmp.json");
//		System.out.println(st);
//		IvaTrafficLightEvent i = JsonUtil.parse(st, IvaTrafficLightEvent.class);
//		System.out.println(i);
		
//		// Test java
//		Date lastRunDay = DateUtil.newDate("20012014", "ddMMyyyy");
//		Date eventDay = DateUtil.newDate("25012014", "ddMMyyyy");
//		Calendar start = Calendar.getInstance();
//		start.setTime(lastRunDay);
//		Calendar end = Calendar.getInstance();
//		end.setTime(eventDay);
//
//		for (Date date = start.getTime(); start.before(end) || start.equals(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
//		    System.out.println(date);
//		}
				
//		User u = new User();
//		u.setUsername("việt nam đẹp tươi");
//		u.setSystemPermission(1);
//		new UserDAO().save(u);
//		
//		System.out.println(new UserDAO().findByUserName("huyda"));
		//new vn.vivas.vtask.model.UserDAO().save(u);
		
		//MailUtil.sendMail("anhpd@vivas.vn", "test email", "Xin chào bạn");
		
		//MailService.testPrint();
		
//		String st = new String(Files.readAllBytes(
//			    Paths.get(ClassLoader.getSystemClassLoader().getResource("mail_template.properties").toURI())));
//		System.out.println(st);
		
		//System.out.println(new UserDAO().query().toList());
		//System.out.println(new GroupDAO().findGroupByUserIdOrderbyGroupName(1));
		//System.out.println(UserService.findAllUserDto().get(0));
		//System.out.println(GroupServiceUtil.findGroupByUserIdOrderbyGroupName(1));
		
		
		//System.out.println(UserGroupServiceUtil.findGroupByUserId(1));
		
	}

	static void testThingsBoard() {
//		// ThingsBoard REST API URL
//		String url = "http://113.190.243.86:48080/";
//
//		// Default Tenant Administrator credentials
//		String username = "tenant@thingsboard.org";
//		String password = "tenant";
//
//		// Creating new rest client and auth with credentials
//		@SuppressWarnings("resource")
//		RestClient client = new RestClient(url);
//		client.login(username, password);
//
//		var c = client.getTenantAssets(new PageLink(1000, 0), "ChargingStation");
//		Asset a = c.getData().get(0);
//		System.out.println(c.getTotalElements());
//		System.out.println("Asset a: " + a.toString());
//		var i = client.getAssetInfoById(c.getData().get(0).getId()).get(); 	// only has title & isPublic
//		System.out.println(i);
//		
//		var key = client.getAttributeKeys(a.getId());		// must get attributes keys for each asset, then get value
//		System.out.println(key);
//		System.out.println(client.getAttributeKvEntries(a.getId(), client.getAttributeKeys(a.getId())));
//		
//		// Find all devices of one device profile
////		System.out.println(client.getTenantDevices("thermostat", new PageLink(1000,0)).getData());  // type = device_profile
//		
//		// get devices belong to a device (asset)
//		DeviceSearchQuery d = new DeviceSearchQuery();
//		d.setParameters(new RelationsSearchParameters(a.getId(), EntitySearchDirection.FROM, 1, RelationTypeGroup.COMMON, true));
//		d.setRelationType("Contains");		// must be "Contains", not "contains"!!!
//		List<String> dtl = Stream.of("default").collect(Collectors.toList());
//		d.setDeviceTypes(dtl);
//		System.out.println(client.findByQuery(d));
		
		
//		// Creating an Asset
//		Asset asset = new Asset();
//		asset.setName("Building 1");
//		asset.setType("ChargingStation");
//		asset = client.saveAsset(asset);
//
//		// creating a Device
//		Device device = new Device();
//		device.setName("Thermometer 1");
//		device.setType("thermometer");
//		device = client.saveDevice(device);
//
//		// creating relations from device to asset
//		EntityRelation relation = new EntityRelation();
//		relation.setFrom(asset.getId());
//		relation.setTo(device.getId());
//		relation.setType("Contains");
//		client.saveRelation(relation);
	}
}
