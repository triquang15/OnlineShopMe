package com.triquang.admin.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.triquang.common.entity.Role;
import com.triquang.common.entity.User;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public List<User> listAll(){
		return (List<User>) userRepository.findAll();
	}
	
	public List<Role> listRole(){
		return (List<Role>) roleRepository.findAll();
		
	}

	public void save(User user) {
		encoderPassword(user);
		userRepository.save(user);
		
	}
	
	public void encoderPassword(User user) {
		String encoderPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encoderPassword);
	}
	
}
