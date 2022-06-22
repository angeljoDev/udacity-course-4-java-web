package com.example.demo;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SareetaApplicationTests {
	@InjectMocks
	private UserController userController;

	@Mock
	private UserRepository userRepository;

	@Mock
	private CartRepository cartRepository;

	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
	}
	@Test
	public void contextLoads() {
	}

	 @Test
	public void createUSer_HappyPath(){
		when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("thisIsHashed");

		 CreateUserRequest r = new CreateUserRequest();
		 r.setUsername("test");
		 r.setPassword("testPassword");
		 r.setConfirmPassword("testPassword");

		 ResponseEntity<User> response = userController.createUser(r);

		 assertEquals(200, response.getStatusCodeValue());

		 User u = response.getBody();
		 assert u != null;
		 assertEquals("test", u.getUsername());
		 assertEquals("thisIsHashed", u.getPassword());
	 }
	@Test
	public void createUser_PasswordsDoNotMatch() {
		CreateUserRequest r = new CreateUserRequest();
		r.setUsername("test");
		r.setPassword("testPassword");
		r.setConfirmPassword("wrongPassword");

		ResponseEntity<User> response = userController.createUser(r);

		assertEquals(400, response.getStatusCodeValue());
	}
	@Test
	public void findUserById_HappyPath() {
		User user = new User();
		user.setId(0L);
		user.setUsername("test");

		when(userRepository.findById(0L)).thenReturn(java.util.Optional.of(user));

		ResponseEntity<User> response = userController.findById(0L);

		assertEquals(200, response.getStatusCodeValue());

		User u = response.getBody();
		assert u != null;
		assertEquals(0L, u.getId());
		assertEquals("test", u.getUsername());
	}

	@Test
	public void findUserByUserName_HappyPath() {
		User user = new User();
		user.setId(0L);
		user.setUsername("test");

		when(userRepository.findByUsername("test")).thenReturn(user);

		ResponseEntity<User> response = userController.findByUserName("test");

		assertEquals(200, response.getStatusCodeValue());

		User u = response.getBody();
		assert u != null;
		assertEquals(0L, u.getId());
		assertEquals("test", u.getUsername());
	}


}
