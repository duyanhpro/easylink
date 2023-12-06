package easylink.service;

import java.util.List;

import easylink.dto.IUserDto;
import easylink.entity.User;
import easylink.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import easylink.exception.AccessDeniedException;
import easylink.exception.NotFoundException;
import easylink.repository.UserRepository;
import easylink.security.CustomPasswordEncoder;
import easylink.security.SecurityUtil;
import easylink.repository.GroupRepository;
import easylink.repository.RoleRepository;
import easylink.repository.UserGroupRepository;

@Service
public class UserService {
	static Logger log = LoggerFactory.getLogger(UserService.class);

	@Autowired
    UserRepository repo;

	@Autowired
	GroupRepository groupRepo;

	@Autowired
	UserGroupRepository ugRepo;

	@Autowired
	RoleRepository roleRepo;

	@Autowired
	RolePermissionService rpService;

	@Autowired
    CustomPasswordEncoder passwordEncoder;

	public User findById(int userId) {
		return repo.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
	}

	public List<User> findAll() {
		return repo.findAllByOrderByUsernameAsc();
		//return repo.findAll(new Sort(Sort.Direction.ASC, "username"));
	}
	public List<IUserDto> findAllMyUserDTO() {
		// Must find users of all children group (direct users)
		//return repo.findAllMyUserOrderByUsernameAsc(SecurityUtil.getUserDetail().getUserId());
		return repo.findAllMyUserWithGroupAndRole(SecurityUtil.getUserDetail().getUserId());
	}

	@Transactional
	public void deleteById(int id) {

		// delete user_group link
		repo.deleteUserGroupByUserId(id);
		// delete user_role link
		repo.deleteUserRoleByUserId(id);

		repo.deleteById(id);
	}

	@Transactional
	public User createNewUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		repo.save(user);
		return user;
	}

	@Transactional
	public User update(User user) {

		if (user.getPassword()!=null)
			user.setPassword(passwordEncoder.encode(user.getPassword()));

		User u = findById(user.getId());
		if (u==null) throw new NotFoundException("User not found");
		BeanUtil.merge(u, user);
		return repo.save(u);
	}

	@Transactional
	public User save(User user) {
		return repo.save(user);
	}
	
	@Transactional
	public User update(String username, User user) {

		if (user.getPassword()!=null)
			user.setPassword(passwordEncoder.encode(user.getPassword()));

		User u = repo.findByUsername(username);
		if (u==null) throw new NotFoundException("User not found");
		BeanUtil.merge(u, user);
		return repo.save(u);
	}

//	public Page<User> findByUsernameSimilar(String username, int page, int pageSize) {
//		if (StringUtil.isNotBlank(username))
//			return repo.findByUsernameLike("%" + username + "%", PageRequest.of(page-1, pageSize));
//		else
//			return repo.findByUsernameLike(null, PageRequest.of(page-1, pageSize));
//		//			return dao.findAll(page);
//	}

//	public Page<UserWithGroupRole> findUserWithGroupAndRole(int page, int pageSize) {
//
//		Page<User> pu = repo.findAll(PageRequest.of(page-1, pageSize));
//		List<UserWithGroupRole> lud = new ArrayList<>();
//
//		for (User u: pu.getContent()) {
//			UserWithGroupRole ud = new UserWithGroupRole();
//			ud.setId(u.getId());
//			ud.setUsername(u.getUsername());
//			ud.setStatus(u.getStatus());
//
//			List<Group> groups = ugRepo.findAllGroupByUserId(u.getId());
//			for (Group g: groups) {
//				ud.getGroupNames().add(g.getName());
////				List<Role> roleSet = rpService.findRoleOfGroup(g);
////				for (Role r: roleSet)
////					ud.getRoleNames().add(r.getName());
//			}
//			lud.add(ud);
//		}
//
//		Page<UserWithGroupRole> pud = new PageImpl<UserWithGroupRole>(lud, PageRequest.of(page-1, pageSize), pu.getTotalElements());
//
//		return pud;
//	}

	/** Authenticate user 
	 *  Throw exception if authenticate fail
	 * */
	public void authenticate(String username, String password) {

		User userDb = repo.findByUsername(username);
		if (userDb == null) {
			throw new AccessDeniedException("Tên đăng nhập hoặc mật khẩu không đúng!");
		}
		if (userDb.getStatus() == User.STATUS_INACTIVE)
			throw new AccessDeniedException("Không thể đăng nhập do tài khoản đang bị khóa.");

		if (passwordEncoder.matches(password, userDb.getPassword()))
			return;

		throw new AccessDeniedException("Tên đăng nhập hoặc mật khẩu không đúng!");

	}

	public User findByUsername(String username) {
		return repo.findByUsername(username);
	}

	@Transactional
	public User changePassword(String oldPassword, String newPassword) {
		User u = SecurityUtil.getUserDetail().getUser();
		
		boolean oldPassMatch = passwordEncoder.matches(oldPassword, u.getPassword());
		if (oldPassMatch)  {
			u.setPassword(passwordEncoder.encode(newPassword));
			repo.save(u);
			return u;
		} else {
			throw new RuntimeException("Current password does not match");
		}
	}

	@Transactional
	public void deleteByUsername(String username) {
		repo.deleteByUsername(username);
	}

	public void resetPassword(int id) {
		User u = findById(id);
		if (u==null)
			throw new NotFoundException("Can not find user");
		u.setPassword(passwordEncoder.encode("123456"));
		repo.save(u);
	}

	public List<User> findAllActive() {
		return repo.findAllActiveSortByName();
	}

	public List<User> findAllUserByType(int userType) {
		return repo.findByType(userType);
	}
}
