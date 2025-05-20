package com.paymybuddy.api.config;

import java.math.BigDecimal;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.api.model.Transfer;
import com.paymybuddy.api.model.User;
import com.paymybuddy.api.repositories.TransferRepository;
import com.paymybuddy.api.repositories.UserRepository;

@Component
public class InitService {
	private static final Logger logger = LogManager.getLogger(DataInitializer.class);
	private final UserRepository userRepository;
	private final TransferRepository transferRepository;

	public InitService(UserRepository userRepository, TransferRepository transferRepository) {
		this.userRepository = userRepository;
		this.transferRepository = transferRepository;
	}

	@Transactional(rollbackFor = Exception.class) // voir P2C3 et doc
	public void initDataSet() throws Exception {
		if (userRepository.count() == 0) {
			logger.info("Inserting data into table user");
			BigDecimal initialBalance = new BigDecimal(100.00);

			// create and save users
			User user1 = User.forInitialData(null, "Georgia", "georgia@email.com", initialBalance,
					"any-hashed-password");
			User user2 = User.forInitialData(null, "Tanka", "tanka@email.com", initialBalance, "any-hashed-password");
			User user3 = User.forInitialData(null, "Bagheera", "bagheera@email.com", initialBalance,
					"any-hashed-password");
			User user4 = User.forInitialData(null, "Mania", "mania@email.com", initialBalance, "any-hashed-password");
			User user5 = User.forInitialData(null, "Jeena", "jeena@email.com", initialBalance, "any-hashed-password");

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
				logger.info("Inserting data into table user_beneficiary");
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
			logger.info("Inserting data into table transfer");
			logger.info("Updating data in table user");
			BigDecimal amount = new BigDecimal(180);
			BigDecimal fees = new BigDecimal(0.005);

			if (optUser1.isPresent() && optUser2.isPresent()) {
				User user1 = optUser1.get();
				User user2 = optUser2.get();

				Transfer transfer = Transfer.forInitialData(user1, user2, "entrée parc aquatique", amount);

				BigDecimal amountToDebit = amount.add(amount.multiply(fees));

				if (amountToDebit.compareTo(user1.getBalance()) == 1) {
					logger.error("The user account balance is insufficient for this transaction");
					throw new IllegalArgumentException("The user account balance is insufficient for this transaction");
				}
				user1.setBalance(user1.getBalance().subtract(amountToDebit));
				user2.setBalance(user2.getBalance().add(amount));

				transferRepository.save(transfer);
				userRepository.save(user1);
				userRepository.save(user2);
			}
		}
	}
}
