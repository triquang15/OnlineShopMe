package com.triquang.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.triquang.common.entity.Role;
import com.triquang.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {
	@Autowired
	private UserRepository repository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateUser() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User user = new User("triquang@gmail.com", "1234567", "Tri", "Quang");
		user.addRole(roleAdmin);

		User saveUser = repository.save(user);
		assertThat(saveUser.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateUser2() {
		User user2 = new User("Ronaldo@gmail.com", "1234567", "Cris", "Ronaldo");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);

		user2.addRole(roleAssistant);
		user2.addRole(roleEditor);

		User savedUser = repository.save(user2);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}

	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers = repository.findAll();
		listUsers.forEach(user -> System.out.println(user));

	}

	@Test
	public void testGetUserById() {
		User user = repository.findById(1).get();
		System.out.println(user);

		assertThat(user).isNotNull();
	}

	@Test
	public void testUpdateUserDetail() {
		User user = repository.findById(1).get();
		user.setEnabled(true);
		user.setEmail("triquang.15qt@gmail.com");

		repository.save(user);
	}

	@Test
	public void testUpdateUserRoles() {
		User user = repository.findById(2).get();
		Role roleAssistant = new Role(5);
		Role roleEditor = new Role(3);
		user.getRoles().remove(roleAssistant);
		user.addRole(roleEditor);

		repository.save(user);
	}

	@Test
	public void testDeleteUser() {
		Integer userId = 2;
		repository.deleteById(userId);

	}
	
	@Test
	public void testGetByEmail() {
		String email = "aptech@gmail.com";
		User user = repository.getUserByEmail(email);
		
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testCountById() {
		Integer id = 4;
		Long countById = repository.countById(id);
		
		assertThat(countById).isNotNull().isGreaterThan(0);
	}
	
}
