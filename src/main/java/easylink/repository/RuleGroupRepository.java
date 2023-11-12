package easylink.repository;

import easylink.entity.RuleDevice;
import easylink.entity.RuleGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RuleGroupRepository extends JpaRepository<RuleGroup, Integer> {

}
