package easylink.security;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.AuditorAware;

/**
 * This class is to enable auto-fill created_user and updated_user in BaseEntity
 * @author Pham Duy Anh
 *
 */
public class AuditorAwareImpl implements AuditorAware<Integer> {

	static Logger log = LoggerFactory.getLogger(AuditorAwareImpl.class);

	@Override
	public Optional<Integer> getCurrentAuditor() {
		return Optional.of(SecurityUtil.getUserDetail()!=null? SecurityUtil.getUserDetail().getUserId() : -1);

	}

}
