package com.paymybuddy.api.config;

import java.math.BigDecimal;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.paymybuddy.api.model.Transfer;
import com.paymybuddy.api.model.User;
import com.paymybuddy.api.repositories.TransferRepository;
import com.paymybuddy.api.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Component
public class DataInitializer {
	private static final Logger logger = LogManager.getLogger(DataInitializer.class); 
	private final UserRepository userRepository;
	private final TransferRepository transferRepository;

	public DataInitializer(UserRepository userRepository, TransferRepository transferRepository) {
		this.userRepository = userRepository;
		this.transferRepository = transferRepository;
	}

	@Transactional // voir P2C3 et doc
	@EventListener(ApplicationReadyEvent.class)
	public void initDataSet() throws Exception {
		try {
			if (userRepository.count() == 0) {
				BigDecimal initialBalance = new BigDecimal(100.00);

				// create and save users
				User user1 = User.forInitialData(null, "Georgia", "georgia@email.com", initialBalance,
						"any-hashed-password");
				User user2 = User.forInitialData(null, "Tanka", "tanka@email.com", initialBalance,
						"any-hashed-password");
				User user3 = User.forInitialData(null, "Bagheera", "bagheera@email.com", initialBalance,
						"any-hashed-password");
				User user4 = User.forInitialData(null, "Mania", "mania@email.com", initialBalance,
						"any-hashed-password");
				User user5 = User.forInitialData(null, "Jeena", "jeena@email.com", initialBalance,
						"any-hashed-password");
			
				userRepository.save(user1);
				userRepository.save(user2);
				userRepository.save(user3);
				userRepository.save(user4);
				userRepository.save(user5);
			}

			// create and save beneficiaries
			Optional<User> optUser1 = userRepository.findById(1);
			Optional<User> optUser2 = userRepository.findById(2);
			Optional<User> optUser3 = userRepository.findById(3);
			Optional<User> optUser4 = userRepository.findById(4);

			if (optUser1.isPresent() && optUser2.isPresent() && optUser3.isPresent()) {
				User user1 = optUser1.get();
				User user2 = optUser2.get();
				User user3 = optUser3.get();

				if (user1.getBeneficiaries().isEmpty()) {
					user1.addBeneficiary(user2);
					user1.addBeneficiary(user3);
					userRepository.save(user1);
				}
			}
			
			if (optUser3.isPresent() && optUser4.isPresent()) {
				User user3 = optUser3.get();
				User user4 = optUser4.get();
				if (user3.getBeneficiaries().isEmpty()) {
					user3.addBeneficiary(user4);
					userRepository.save(user3);
				}
			}
			
			if (transferRepository.count() == 0) {
				BigDecimal amount = new BigDecimal(18);
				Transfer transfer = Transfer.forInitialData(optUser1.get(), optUser2.get(), "entrée parc aquatique", amount);
				transferRepository.save(transfer);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
}
