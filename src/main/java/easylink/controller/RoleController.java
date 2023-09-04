package easylink.controller;

import java.util.ArrayList;

import easylink.common.Constant;
import easylink.entity.Permission;
import easylink.entity.Role;
import easylink.security.NeedRole;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/role")
@NeedRole({Constant.ROLE_ADMIN})		// only admin can CRUD role
public class RoleController extends BaseController {

	// List role
	@GetMapping("")
	public String list(Model model) {
		model.addAttribute("pageTitle", "Danh sách quyền");
		model.addAttribute("roles", rpService.listAllRole());
		return "role/list";
	}

	@GetMapping("/view/{id}")
	public String getRole(Model model, @PathVariable int id) {
		model.addAttribute("pageTitle", "Chi tiết quyền");
		model.addAttribute("action", "update");
		model.addAttribute("role", rpService.findRoleById(id));
		model.addAttribute("allRoles", rpService.listAllRole());
		model.addAttribute("permissions", rpService.findPermissionOfRole(id, true));
		model.addAttribute("parentRoles", rpService.findParentRoleNames(id));
		return "role/edit";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(Model model, @PathVariable int id, RedirectAttributes redirectAttrs) {
		rpService.deleteRole(id);
		redirectAttrs.addFlashAttribute("infoMsg", localeService.getMessage("role.delete.success"));
		return "redirect:/role";
	}

	@GetMapping("/create")
	public String createRole(Model model) {
		model.addAttribute("pageTitle", "Tạo mới quyền");
		model.addAttribute("action", "create");
		model.addAttribute("role", new Role());
		model.addAttribute("parentRoles", new ArrayList<String>());
		model.addAttribute("allRoles", rpService.listAllRole());
		return "role/edit";
	}

	
	// Create new if id != null, else update
	@PostMapping("/save")
	public String save(Model model, @ModelAttribute Role role, Integer[] parentRoleIds,
			boolean isFormChanged, boolean isParentRolesChanged,
			RedirectAttributes redirectAttrs) {
		log.info("Creating/Updating role {}", role);
		if (isFormChanged) {
			
			if (role.getStatus() == null) role.setStatus(Role.STATUS_INACTIVE);
			if (role.getId()!=null && role.getId()!=0) {
				role = rpService.saveOrUpdateRole(role);
				redirectAttrs.addFlashAttribute("infoMsg", "Role update successfully");
			}
			else { 
				role = rpService.saveOrUpdateRole(role);
				redirectAttrs.addFlashAttribute("infoMsg", "Role created successfully");
			}

			// Update groups for this user
			if (isParentRolesChanged) {
				log.info("Update parent roles. Parent roles' ID:  " + parentRoleIds);
				rpService.updateParentRoles(role.getId(), parentRoleIds);
			}
			
		} else {
			log.info("No change was made");
		}
		return "redirect:/role/view/" + role.getId();
	}
	
	@GetMapping("/permission")
	public String getPermissions(Model model, int roleId) {
		Role r = rpService.findRoleById(roleId);
		model.addAttribute("pageTitle", "Danh sách Permission của quyền " + r.getName());
		model.addAttribute("role", r);
		model.addAttribute("permissions", rpService.getSortedPermissionList(roleId, false));
		return "role/perm-list";
	}
	
	@GetMapping("/removePermission")
	public String removePermissions(Model model, int roleId, int permissionId) {
		rpService.deletePermssionFromRole(roleId, permissionId);
		model.addAttribute("role", rpService.findRoleById(roleId));
		model.addAttribute("permissions", rpService.getSortedPermissionList(roleId, false));
		return "role/perm-list";
	}
	
	@PostMapping("/addPermission")
	public String addPermissions(Model model, @RequestParam int roleId, String resource, String action, String condition, String description, boolean allow) {
		// check if permission is valid

		// Create the permission. Use existing if already exist
		Permission p = rpService.insertPermission(resource, action, condition, description, allow);
		if (p != null)
			model.addAttribute("infoMsg", "Permission added successfully");
		else
			model.addAttribute("infoMsg", "Can not add permission");
		
		// Add permission to role
		rpService.addPermissionToRole(roleId, p.getId());
		
		// Reload view
		model.addAttribute("role", rpService.findRoleById(roleId));
		model.addAttribute("permissions", rpService.getSortedPermissionList(roleId, false));
		
		return "role/perm-list";
	}
}
