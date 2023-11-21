package easylink.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import easylink.repository.ConfigParamRepository;
import vn.vivas.core.util.DateUtil;

/**
 * Use a database table to store and manage configuration
 * @author phamd
 *
 */
@Service
public class ConfigParamService {

	static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";
	static final String LAST_SYNC_DATE_PARAM = "LAST_SYNC_DATE";
	static final String PRICE_PER_HOUR = "PRICE_PER_HOUR";
	
	@Autowired
	ConfigParamRepository configRepo;
	
	public Date findLastSyncDate() {
		return DateUtil.newDate((String)configRepo.findParameter(LAST_SYNC_DATE_PARAM), DATE_FORMAT);
	};
	
	public void updateLastSyncDate(Date syncDate) {
		configRepo.saveOrUpdateParameter(LAST_SYNC_DATE_PARAM, DateUtil.date2String(syncDate, DATE_FORMAT), Date.class.getName());
	}
	
	public void updatePricePerHour(Long newPrice) {
		configRepo.saveOrUpdateParameter(PRICE_PER_HOUR, newPrice.toString(), Long.class.getName());
	}
	
	public long findPricePerHour() {
		return (Long)configRepo.findParameter(PRICE_PER_HOUR);
	}
	
	public String getHomeDashboardUrl() {
		return (String) configRepo.findParameter("HOME_DASHBOARD");
	}
	
	public String getDeviceDashboardUrl() {
		return (String) configRepo.findParameter("STATION_DASHBOARD");
	}

	public Object getConfig(String configParam) {
		return configRepo.findParameter(configParam);
	}
	public void updateConfig(String configParam, String value, String type) {
		configRepo.saveOrUpdateParameter(configParam, value, type);
	}
}
