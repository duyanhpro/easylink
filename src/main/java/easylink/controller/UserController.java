package easylink.controller;

import easylink.entity.User;
import easylink.security.NeedPermission;
import easylink.security.SecurityUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

	// List User
	@NeedPermission("user:list")
	@GetMapping("")
	public String list(Model model) {
		model.addAttribute("pageTitle", "Danh sách người dùng");
//		List<User> users;
//		if (SecurityUtil.hasPermission("user", "listAll"))
//		//if (SecurityUtil.hasRole("ROLE_ADMIN"))
//			users = userService.findAll();
//		else
//			users = userService.findAllUserByType(User.TYPE_CUSTOMER);
//		users = userService.findAllMyUser();
		
		model.addAttribute("users", userService.findAllMyUserDTO());
		return "user/list";
	}

	@NeedPermission("user:create")
	@GetMapping("/create")
	public String create(Model model) {
		log.debug("Create new user");
		model.addAttribute("pageTitle", "Tạo mới người dùng");
		model.addAttribute("action", "create");
		model.addAttribute("user", new User());
		model.addAttribute("allGroups", groupService.findAllMyGroups());
		model.addAttribute("allRoles", rpService.findAllRole());
		return "user/edit";
	}
	
	// View user detail
	@GetMapping("/view/{id}")
//	@NeedPermission("user:view")  --> don't need because we check inside for each object
	public String getUser(Model model,@PathVariable int id) {
		log.debug("Showing detail of user id {}", id);
		
		model.addAttribute("action", "update");
		User u = userService.findById(id);
	
		SecurityUtil.authorize("user", "view", u);
		
		model.addAttribute("pageTitle", "Chi tiết người dùng " + u.getUsername());
		model.addAttribute("user", u);
		model.addAttribute("allGroups", groupService.findAllMyGroups());
		model.addAttribute("userGroups", ugService.findGroupNameByUserId(id));
		model.addAttribute("allRoles", rpService.findAllRole());
		model.addAttribute("userRoleNames", rpService.findDirectRoleNameOfUser(id));
			
		return "user/edit";
	}

	@GetMapping("/profile")
	public String getProfile(Model model) {
		log.debug("Showing personal profile");

		model.addAttribute("action", "profile");
		User u = SecurityUtil.getUserDetail().getUser();

		model.addAttribute("pageTitle", "Thông tin cá nhân");
		model.addAttribute("user", u);
		model.addAttribute("allGroups", groupService.findAllMyGroups());
		model.addAttribute("userGroups", ugService.findGroupNameByUserId(u.getId()));
		model.addAttribute("allRoles", rpService.findAllRole());
		model.addAttribute("userRoleNames", rpService.findDirectRoleNameOfUser(u.getId()));

		return "user/edit";
	}

	// User Create/Update
	// Create new if id != null, else update, else update profile
//	@NeedPermission("user:create,update")  //--> don't need because we check inside for each object
	@PostMapping("/save")
	public String save(Model model, @ModelAttribute User user, Integer[] groupIds, Integer[] roleIds,
			boolean isFormChanged, boolean isUserGroupChanged, boolean isUserRoleChanged, 
			RedirectAttributes redirectAttrs, String action) {
		log.info("Creating/Updating user {}", user.getUsername());
		log.debug("User {}", user);
		if (isFormChanged) {
			if (action.equals("update")) {
				
				SecurityUtil.authorize("user", "update", user);
				
				userService.update(user);	// merge updated fields
				SecurityUtil.getUserDetail().setUser(user);	// refresh user in SecurityUtil session
				redirectAttrs.addFlashAttribute("infoMsg", localeService.getMessage("user.update.success"));
			}
			else if (action.equals("create")){
				SecurityUtil.authorize("user", "create", user);
				
				userService.createNewUser(user);
				isUserGroupChanged = true;
				isUserRoleChanged = true;	// force save role, group when create new user
				redirectAttrs.addFlashAttribute("infoMsg", localeService.getMessage("user.create.success"));
			} else if (action.equals("profile")) {
				User ori = SecurityUtil.getUserDetail().getUser();
				ori.setFullName(user.getFullName());
				ori.setPhone(user.getPhone());
				ori.setEmail(user.getEmail());
				userService.save(ori);
				SecurityUtil.getUserDetail().setUser(ori);
				redirectAttrs.addFlashAttribute("infoMsg", localeService.getMessage("user.update.success"));
			}
			
			// Update groups for this user
			if (isUserGroupChanged)
				ugService.updateGroupForUser(user, groupIds);
			
			if (isUserRoleChanged) 
				rpService.updateRolesForUser(user.getId(), roleIds);
			
		} else {
			log.info("No change was made");
		}
		//return "redirect:/user/view/" + user.getId();
		if (action.equals("profile"))
			return "redirect:/user/profile";
		else return "redirect:/user";
	}

	// Delete user
//	@NeedPermission("user:delete")  //--> don't need because we check inside for each object
	@GetMapping("/delete/{id}")
	public String deleteUser(Model model, @PathVariable int id, RedirectAttributes redirectAttrs) {
		log.debug("Delete user id {}", id);
		if (id == SecurityUtil.getUserDetail().getUserId()) {
			throw new RuntimeException("Không thể xóa tài khoản của chính mình!");
		}
		SecurityUtil.authorize("user", "delete", userService.findById(id));
		
		userService.deleteById(id);
		redirectAttrs.addFlashAttribute("infoMsg", localeService.getMessage("user.delete.success"));
		return "redirect:/user/";
	}
	
	// Delete user
	@NeedPermission("user:resetPassword")
	@GetMapping("/resetPassword/{id}")
	public String resetPasswordUser(Model model, @PathVariable int id, RedirectAttributes redirectAttrs) {
		log.debug("Reset password for user id {}", id);
		
		SecurityUtil.authorize("user", "resetPassword", userService.findById(id));
		
		userService.resetPassword(id);
		
		redirectAttrs.addFlashAttribute("infoMsg", localeService.getMessage("user.reset.success"));
		return "redirect:/user/view/" + id;
	}

	@GetMapping("/changePassword")
	public String openChangePasswordPage(Model model, RedirectAttributes redirectAttrs, String oldPassword, String newPassword) {
		return "user/changePassword";
	}
	
	@PostMapping("/changePassword")
	public String changePasswordUser(Model model, RedirectAttributes redirectAttrs, String oldPassword, String newPassword) {
		log.debug("Change password for user id {}", SecurityUtil.getUserDetail().getUsername());
		userService.changePassword(oldPassword, newPassword);
		
		redirectAttrs.addFlashAttribute("infoMsg", "Đổi mật khẩu thành công");
		return "redirect:/user/changePassword";
	}
	
}
