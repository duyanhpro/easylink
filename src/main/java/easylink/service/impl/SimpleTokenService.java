package easylink.service.impl;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import easylink.security.UserAuthorizationDetail;
import easylink.service.TokenService;
import net.jodah.expiringmap.ExpiringMap;

/**
 * A very simple token service which store token in a local auto-expired map
 * 
 * @author phamd
 *
 */
@Service
public class SimpleTokenService implements TokenService {

	static Logger log = LoggerFactory.getLogger(SimpleTokenService.class);
	
	@Value("${security.token.expireTime}")
	Long tokenExpireTime;
	
	Map<String, UserAuthorizationDetail> tokenMap;
	
	@PostConstruct
	public void init() {
		tokenMap = ExpiringMap.builder()
				  .expiration(tokenExpireTime, TimeUnit.SECONDS)
				  .build();
	}

	@Override
	public String generateToken(UserAuthorizationDetail userDetail) {
		
		String t = UUID.randomUUID().toString();
		tokenMap.put(t, userDetail);
		
		log.debug("Generated token " + t + " for user " + userDetail.getUser().getUsername());
		return t;
	}

	@Override
	public boolean isTokenExist(String token) {
		if (token == null) return false;
		boolean b = tokenMap.containsKey(token);
		log.debug("Token store has " + tokenMap.size() + " token, and token " + token + 
				(b? "exists":"does not exist"));
		return b;
	}

	@Override
	public void clearToken(String token) {
		tokenMap.remove(token);
	}

	@Override
	public UserAuthorizationDetail getUserDetailFromToken(String token) {
		if (token == null) return null;
		return tokenMap.get(token);
	}

	@Override
	public void updateToken(String token, UserAuthorizationDetail userDetail) {
		tokenMap.put(token, userDetail);
	}

}
