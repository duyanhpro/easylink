package easylink.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import easylink.dto.GroupDto;
import easylink.repository.GroupRepositoryExt;

/**
 * Demo using custom DAO query with Spring Data
 * @author phamd
 *
 */
// Don't need @Repository. Spring knows itself!. Put @Repository here will cause circle dependency error
public class GroupRepositoryImpl implements GroupRepositoryExt {

	@PersistenceContext
	EntityManager em;
	
	@Override
	public List<GroupDto> findGroupDtoByUserId(Integer userId) {
		String jql = "select new " + GroupDto.class.getName() + "(g.id, g.name, g.status, r.name) "
				+ " from Group g, Role r, UserGroup ug"
				+ " where ug.userId = :userId and g.id = ug.groupId and r.id = g.roleId ";
		return em.createQuery(jql).setParameter("userId", userId).getResultList();
	}
	
	@Override
	public List<GroupDto> findAllGroupDto() {
		
		String jpql = "select new " + GroupDto.class.getName() + "(g.id, g.name, g.status, r.name) from "
				+ " Group g, Role r where "
				+ " r.id = g.roleId"; 
		return em.createQuery(jpql).getResultList();
	}
	
}
