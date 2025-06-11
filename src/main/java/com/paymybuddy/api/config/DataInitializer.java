package com.paymybuddy.api.config;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.paymybuddy.api.model.Role;
import com.paymybuddy.api.model.RoleName;
import com.paymybuddy.api.model.User;
import com.paymybuddy.api.repositories.RoleRepository;
import com.paymybuddy.api.repositories.UserRepository;

@Profile(value = { "mysql", "postgresql" })
@Configuration
public class DataInitializer {
	@Value("${admin.default.password}")
	private String adminPassword;

	@Bean
	CommandLineRunner createAdmin(UserRepository repository, RoleRepository roleRepository,
			PasswordEncoder passwordEncoder) {

		return args -> {
			if (repository.findByActiveEmail("admin@email.com").isEmpty()) {
				Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN).orElseThrow();
				User admin = User.forInitialData(null, "Admin", "admin@email.com", new BigDecimal("0"),
						passwordEncoder.encode(adminPassword), adminRole);
				
				repository.save(admin);

			}
		};
	}

}
