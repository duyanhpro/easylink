package easylink.service.impl;

import java.util.UUID;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import easylink.security.UserAuthorizationDetail;
import easylink.service.TokenService;
import vn.vivas.core.util.JsonUtil;
import redis.clients.jedis.Jedis;

/**
 *Token service which store token in redis
 * 
 * @author phamd
 *
 */
//@Service
public class RedisTokenService implements TokenService {

	static Logger log = LoggerFactory.getLogger(RedisTokenService.class);
	
	@Value("${security.token.expireTime}")
	Integer tokenExpireTime;
	
	@Value("${redis.server}")
	String redisServer;
	
	@Value("${redis.port}")
	int redisPort;
	
	Jedis jedis;
	
	@PostConstruct
	public void init() {
		// Connect to redis
	    jedis = new Jedis(redisServer, redisPort);
	    
	}

	@Override
	public String generateToken(UserAuthorizationDetail userDetail) {
		
		String t = UUID.randomUUID().toString();
		jedis.set("token-"+ t, JsonUtil.toString(userDetail));
		jedis.expire("token-"+ t, tokenExpireTime);
		
		log.debug("Generated token " + t + " for user " + userDetail.getUser().getUsername());
		return t;
	}

	@Override
	public boolean isTokenExist(String token) {
		if (token == null) return false;
		return jedis.exists("token-" + token);
	}

	@Override
	public void clearToken(String token) {
		jedis.del("token-"+ token);
	}

	@Override
	public UserAuthorizationDetail getUserDetailFromToken(String token) {
		if (token == null) return null;
		String user = jedis.get("token-" + token);
		if (user==null) return null;
		return JsonUtil.parse(user, UserAuthorizationDetail.class);
	}

	@Override
	public void updateToken(String token, UserAuthorizationDetail userDetail) {
		jedis.set("token-"+ token, JsonUtil.toString(userDetail));
		jedis.expire("token-"+ token, tokenExpireTime);
	}

}
