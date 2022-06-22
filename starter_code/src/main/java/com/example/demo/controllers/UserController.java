package com.example.demo.controllers;

import com.example.demo.logging.SplunkLogger;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;




@RestController
@RequestMapping("/api/user")
public class UserController {

	private static final SplunkLogger splunkLogger = new SplunkLogger();
	private UserRepository userRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {

		return ResponseEntity.of(userRepository.findById(id));
	}

	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {

		User user = userRepository.findByUsername(username);
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}

	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
		User user = new User();
		user.setUsername(createUserRequest.getUsername());
		if(!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())){
			splunkLogger.log("Password not match: " + user.getPassword() + "with:" + createUserRequest.getConfirmPassword());
			return  ResponseEntity.badRequest().build();
		}
		try{
			user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
			Cart cart = new Cart();
			cartRepository.save(cart);
			user.setCart(cart);
			userRepository.save(user);
		}catch(Exception e){
			System.out.println("Error: " +e.getCause());
		}
		splunkLogger.log("User created correctly: " + user.getUsername());
		return ResponseEntity.ok(user);

	}
}

