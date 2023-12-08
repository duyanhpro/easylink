package easylink.service;

import java.util.Date;

import easylink.dto.ActionCreateAlarm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import easylink.repository.ConfigParamRepository;
import vn.vivas.core.util.DateUtil;
import vn.vivas.core.util.JsonUtil;

/**
 * Use a database table to store and manage configuration
 * @author phamd
 *
 */
@Service
public class ConfigParamService {

	static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";
	
	@Autowired
	ConfigParamRepository configRepo;

	public Object getConfig(String configParam) {
		return configRepo.findParameter(configParam);
	}
	public void updateConfig(String configParam, String value, String type) {
		configRepo.saveOrUpdateParameter(configParam, value, type);
	}
	@Cacheable("alarmConfig")
	public ActionCreateAlarm getAlarmConfig(String alarmName) {
		String s = configRepo.getValue(alarmName);
		return JsonUtil.parse(s, ActionCreateAlarm.class);
	}
}
