package easylink.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import easylink.entity.ConfigParam;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class ConfigParamRepository  {
	
	static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";
	
	@PersistenceContext
	EntityManager em;
	
	public Object findParameter(String parameterName) {
		String jql = "select u from ConfigParam u where u.name = '" + parameterName + "'";
		try {
			ConfigParam cp = (ConfigParam) em.createQuery(jql).getSingleResult();
			if (cp.getType().equals(Integer.class.getName())) {
				return Integer.parseInt(cp.getValue());
			}
			if (cp.getType().equals(Long.class.getName())) {
				return Long.parseLong(cp.getValue());
			}
			if (cp.getType().equals(Boolean.class.getName())) {
				return Boolean.parseBoolean(cp.getValue());
			}
//			if (cp.getType().equals(Date.class.getName())) {		// Need to sync with the load method in Service
//				return DateUtil.newDate(cp.getValue(), DATE_FORMAT);
//			}
			// TODO: other type
			
			// else: default is String
			return cp.getValue();
		} catch (Exception e) {
			return null;
		}
	}

	public String getValue(String parameterName) {
		String jql = "select u.value from ConfigParam u where u.name = '" + parameterName + "'";
		try {
			String cp = (String) em.createQuery(jql).getSingleResult();
			return cp;
		} catch (Exception e) {
			return null;
		}
	}

	@Transactional
	public void saveOrUpdateParameter(String parameterName, String value, String type) {
		String jql = "select u from ConfigParam u where u.name = '" + parameterName + "'";
		ConfigParam cp;
		try {
			cp = (ConfigParam) em.createQuery(jql).getSingleResult();
		} catch (Exception e) {
			cp = null;
		}
		if (cp == null) {
			cp = new ConfigParam();
			cp.setName(parameterName);
		}
		cp.setType(type);
		cp.setValue(value);

		em.persist(cp);
	}
}
