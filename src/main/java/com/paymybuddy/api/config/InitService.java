package com.paymybuddy.api.config;

import java.math.BigDecimal;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.api.constants.ApiMessages;
import com.paymybuddy.api.constants.ServiceFees;
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
					"$2a$10$Lc2JhT8glUB8.hfGoRYGVuuDnL7RM8XXSLAQTYlmv5hlNkkE14BQu");
			User user2 = User.forInitialData(null, "Tanka", "tanka@email.com", initialBalance,
					"$2a$10$tOwIV5x8bXF/Xh5tkHkKmO153X8bSGkibFU21KK6oshF1R9mVS6KO");
			User user3 = User.forInitialData(null, "Bagheera", "bagheera@email.com", initialBalance,
					"$2a$10$1CLQi6XqmrfmafzzfeO/jOhGfnY6F4vIk5lbyQh7aSKN7VS.0mIdi");
			User user4 = User.forInitialData(null, "Mania", "mania@email.com", initialBalance,
					"$2a$10$OrZrQGi2o7nb1eRzZfgWFOdm9LksYYirAfjb3Agdf9if30eNWhEom");
			User user5 = User.forInitialData(null, "Jeena", "jeena@email.com", initialBalance,
					"$2a$10$E3MsEXGQJfKhtRwBWCQjoeOHrGXH2AqN15RhOjQu1GirCdMcNRrTG");

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
			BigDecimal amount = new BigDecimal(18);

			if (optUser1.isPresent() && optUser2.isPresent()) {
				User user1 = optUser1.get();
				User user2 = optUser2.get();

				Transfer transfer = Transfer.forInitialData(user1, user2, "entrée parc aquatique", amount);

				BigDecimal amountToDebit = amount.add(amount.multiply(ServiceFees.TRANSFER_FEES));

				if (amountToDebit.compareTo(user1.getBalance()) == 1) {
					logger.error(ApiMessages.INSUFFICIENT_BALANCE);
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
