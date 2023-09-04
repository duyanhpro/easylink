package easylink.controller.api;

import easylink.entity.User;
import easylink.security.NeedRole;
import easylink.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class ApiUserController {

	@Autowired
	private UserService userService;

	@NeedRole("ROLE_ADMIN")
	@GetMapping(value = "/users")
	public ResponseEntity<Iterable<User>> listUser() {
		return new ResponseEntity<Iterable<User>>(userService.findAll(), HttpStatus.OK);
	}

	@NeedRole("ROLE_ADMIN")
	@GetMapping(value = "/users/{userId}")
	public ResponseEntity<User> getUserById(@PathVariable(required = true) Integer userId) {
		return new ResponseEntity<User>(userService.findById(userId), HttpStatus.OK);
	}
}
