package easylink.controller;

import easylink.entity.Group;
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
@RequestMapping("/group")
public class GroupController extends BaseController {

	// List & Search Group
	@NeedPermission("group:list")
	@GetMapping("")
	public String list(Model model) {
		model.addAttribute("pageTitle", "Danh sách nhóm");
		model.addAttribute("groups", groupService.findAll());
		return "group/list";
	}

	// List & Search Group
	@NeedPermission("group:tree")
	@GetMapping("/tree")
	public String listTree(Model model) {
		model.addAttribute("pageTitle", "Danh sách nhóm");
		model.addAttribute("groups", groupService.findAll());
		return "group/tree";
	}

	// create new group page
	@NeedPermission("group:create")
	@GetMapping("/create")
	public String create(Model model) {
		model.addAttribute("pageTitle", "Tạo mới nhóm");
		model.addAttribute("action", "create");
		model.addAttribute("group", new Group());
		model.addAttribute("allUsers", userService.findAll());	// list all users (used to add user into group)
		model.addAttribute("allGroups", groupService.findAll());	// list all groups (used to select parent)
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
		model.addAttribute("allUsers", userService.findAll());						// list all users (used to add user into group)
		model.addAttribute("allGroups", groupService.findAll());	// list all groups (used to select parent)
		return "group/edit";
	}

	// Create new if id != null, else update
	@PostMapping("/save")
	@NeedPermission("group:update")
	public String save(Model model, @ModelAttribute Group group, Integer[] userIds,
			boolean isFormChanged, boolean isUserGroupChanged, 
			RedirectAttributes redirectAttrs) {
		log.info("Creating/Updating groupr {}", group.getName());
		log.debug("Group {}", group);
		if (isFormChanged) {
			if (group.getId()!=null) {
				SecurityUtil.authorize("group", "update", group);
				groupService.update(group.getId(), group);
				redirectAttrs.addFlashAttribute("infoMsg", localeService.getMessage("group.update.success"));
			} else { 
				SecurityUtil.authorize("group", "create", group);
				groupService.create(group);
				redirectAttrs.addFlashAttribute("infoMsg", localeService.getMessage("group.update.success"));
			}
			
			// Update users in group
			if (isUserGroupChanged)
				ugService.updateUserForGroup(group, userIds);

		} else {
			log.info("No change was made");
		}
		return "redirect:/group/view/" + group.getId();
	}
	
	// Delete user
	@GetMapping("/delete/{id}")
//	@NeedPermission("group:delete")
	public String deleteGroup(Model model, @PathVariable int id, RedirectAttributes redirectAttrs) {
		log.debug("Delete group id {}", id);
		SecurityUtil.authorize("group", "delete", groupService.findById(id));
		groupService.deleteById(id);
		redirectAttrs.addFlashAttribute("infoMsg", localeService.getMessage("group.delete.success"));
		return "redirect:/group";
	}
	
}
