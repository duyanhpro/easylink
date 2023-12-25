package easylink.controller;

import easylink.entity.Group;
import easylink.exception.AccessDeniedException;
import easylink.security.NeedPermission;
import easylink.security.SecurityUtil;
import easylink.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/group")
public class GroupController extends BaseController {

	@Autowired
	DeviceService deviceService;
	// List & Search Group
//	@NeedPermission("group:list")
//	@GetMapping("")
//	public String list(Model model) {
//		model.addAttribute("pageTitle", "Danh sách nhóm");
//		model.addAttribute("groups", groupService.findAll());
//		return "group/list";
//	}

	// List & Search Group
	@NeedPermission("group:list")
	@GetMapping("")
	public String listTree(Model model) {
		model.addAttribute("pageTitle", "Danh sách nhóm");
		model.addAttribute("groups", groupService.findAllMyGroups());
		return "group/tree";
	}

	// create new group page
	@NeedPermission("group:create")
	@GetMapping("/create")
	public String create(Model model) {
		model.addAttribute("pageTitle", "Tạo mới nhóm");
		model.addAttribute("action", "create");
		model.addAttribute("group", new Group());
		model.addAttribute("allUsers", userService.findAllMyUserDTO());	// list all users (used to add user into group)
		model.addAttribute("allGroups", groupService.findAllMyGroups());	// list all groups (used to select parent)
		model.addAttribute("allDevices", deviceService.findAllMyDevices());
		return "group/edit";
	}
	
	// view group detail
	@GetMapping("/view/{id}")
//	@NeedPermission("group:read")
	public String getGroup(Model model, @PathVariable int id) {
		model.addAttribute("pageTitle", "Chi tiết nhóm");
		Group g = groupService.findById(id);
		log.debug("Find group detail: " + g);
		
		SecurityUtil.authorize("group", "read", g);
		
		model.addAttribute("action", "view");
		model.addAttribute("group", g);
		model.addAttribute("usernames", ugService.findUserNameByGroupId(id));	// list usernames in group
		model.addAttribute("allUsers", userService.findAllMyUserDTO());						// list all users (used to add user into group)
		//model.addAttribute("allGroups", groupService.findAllMyGroups());	// list all groups (used to select parent)
		model.addAttribute("allGroups", groupService.findAllGroupAllowedToBeParent(id));

		model.addAttribute("allDevices", deviceService.findAllMyDevices());
		return "group/edit";
	}

	// Create new if id != null, else update
	@PostMapping("/save")
	@NeedPermission("group:update")
	public String save(Model model, @ModelAttribute Group group, Integer[] userIds,
			boolean isFormChanged, boolean isUserGroupChanged, 
			RedirectAttributes redirectAttrs) {
		log.info("Creating/Updating group {}", group.getName());
		log.debug("Group {}", group);
		if (group.getId() == null) {
			// create
			SecurityUtil.authorize("group", "create", group);
			groupService.create(group);
			redirectAttrs.addFlashAttribute("infoMsg", localeService.getMessage("group.update.success"));
			ugService.updateUserForGroup(group, userIds);
		} else if (isFormChanged) {
			// update
			SecurityUtil.authorize("group", "update", group);
			if (!groupService.checkPermission(group.getId()))
				throw new AccessDeniedException("Người dùng không có quyền với nhóm này");

			groupService.update(group.getId(), group);
			redirectAttrs.addFlashAttribute("infoMsg", localeService.getMessage("group.update.success"));

			// Update users in group
			if (isUserGroupChanged)
				ugService.updateUserForGroup(group, userIds);

		} else {
			log.info("No change was made");
		}
		//return "redirect:/group/view/" + group.getId();
		return "redirect:/group";
	}
	
	// Delete user
	@GetMapping("/delete/{id}")
//	@NeedPermission("group:delete")
	public String deleteGroup(Model model, @PathVariable int id, RedirectAttributes redirectAttrs) {
		log.debug("Delete group id {}", id);
		SecurityUtil.authorize("group", "delete", groupService.findById(id));

		groupService.deleteAndUpdateTree(id);		// check policy in this method
		redirectAttrs.addFlashAttribute("infoMsg", localeService.getMessage("group.delete.success"));
		return "redirect:/group";
	}
	
}
