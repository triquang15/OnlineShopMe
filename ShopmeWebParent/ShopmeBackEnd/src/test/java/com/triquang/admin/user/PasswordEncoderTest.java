package com.triquang.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {

	@Test
	public void testEncoderPassword() {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String rawPasword = "12345678";
		String encodedPassword = passwordEncoder.encode(rawPasword);

		System.out.println(encodedPassword);

		boolean matches = passwordEncoder.matches(rawPasword, encodedPassword);

		assertThat(matches).isTrue();
	}
}
