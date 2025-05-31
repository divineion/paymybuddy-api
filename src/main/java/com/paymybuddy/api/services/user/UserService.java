package com.paymybuddy.api.services.user;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.api.constants.ApiMessages;
import com.paymybuddy.api.constants.UserManagementSettings;
import com.paymybuddy.api.exceptions.EmailAlreadyExistsException;
import com.paymybuddy.api.exceptions.EmailNotFoundException;
import com.paymybuddy.api.exceptions.ForbiddenAccessException;
import com.paymybuddy.api.exceptions.PasswordMissmatchException;
import com.paymybuddy.api.exceptions.RelationAlreadyExistsException;
import com.paymybuddy.api.exceptions.RelationNotFoundException;
import com.paymybuddy.api.exceptions.SameEmailException;
import com.paymybuddy.api.exceptions.SamePasswordException;
import com.paymybuddy.api.exceptions.SelfRelationException;
import com.paymybuddy.api.exceptions.UserAlreadySoftDeleted;
import com.paymybuddy.api.exceptions.UserDeletionNotAllowedException;
import com.paymybuddy.api.exceptions.UserNotFoundException;
import com.paymybuddy.api.exceptions.UserNotSoftDeletedException;
import com.paymybuddy.api.model.User;
import com.paymybuddy.api.repositories.UserRepository;
import com.paymybuddy.api.services.dto.BeneficiaryDto;
import com.paymybuddy.api.services.dto.ChangeEmailDto;
import com.paymybuddy.api.services.dto.ChangePasswordDto;
import com.paymybuddy.api.services.dto.EmailRequestDto;
import com.paymybuddy.api.services.dto.TransferDto;
import com.paymybuddy.api.services.dto.TransferPageDto;
import com.paymybuddy.api.services.dto.UserAccountDto;
import com.paymybuddy.api.services.dto.UserDto;
import com.paymybuddy.api.services.transfer.TransferMapper;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final UserMapper mapper;
	private final TransferMapper transferMapper;
	private final UserValidator validator;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, UserMapper mapper, TransferMapper transferMapper,
			UserValidator validator, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.mapper = mapper;
		this.transferMapper = transferMapper;
		this.validator = validator;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional(rollbackFor = Exception.class)
	public UserDto registerNewUserAccount(UserAccountDto accountDto) throws EmailAlreadyExistsException {
		if (userRepository.findByActiveEmail(accountDto.email()).isPresent()) {
			throw new EmailAlreadyExistsException(ApiMessages.EMAIL_ALREADY_EXISTS + accountDto.email());
		}

		User newUser = mapper.fromUserAccountDtoToUser(accountDto);
		userRepository.save(newUser);

		return mapper.fromUserToUserDto(newUser);
	}

	public UserDto findUserById(int id) throws UserNotFoundException {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException(ApiMessages.USER_NOT_FOUND + id));
		UserDto userDto = mapper.fromUserToUserDto(user);

		return userDto;
	}

	public User findUserByEmail(String email) throws EmailNotFoundException {
		User user = userRepository.findByActiveEmail(email)
				.orElseThrow(() -> new EmailNotFoundException(ApiMessages.EMAIL_NOT_FOUND + email));

		return user;
	}

	public TransferPageDto findUserTransferPageInfo(int id) throws UserNotFoundException {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException(ApiMessages.USER_NOT_FOUND + id));

		List<BeneficiaryDto> beneficiaries = user.getBeneficiaries().stream().map(mapper::fromUserToBeneficiaryDto)
				.sorted(Comparator.comparing(BeneficiaryDto::username)).toList();

		List<TransferDto> receivedTransfers = user.getReceivedTransfers().stream()
				.map(transfer -> transferMapper.fromTransferToTransferDto(transfer, id))
				.sorted(Comparator.comparing(TransferDto::date).reversed()).toList();

		List<TransferDto> sentTransfers = user.getSentTransfers().stream()
				.map(transfer -> transferMapper.fromTransferToTransferDto(transfer, id))
				.sorted(Comparator.comparing(TransferDto::date).reversed()).toList();

		TransferPageDto userTransferInfo = new TransferPageDto(user.getId(), beneficiaries, user.getBalance(),
				sentTransfers, receivedTransfers);

		return userTransferInfo;
	}

	// POST REQUESTS

	// add a relation
	// first of all verify the provided email address exists - calls repo
	// busines validation (no duplicates, no self relation
	// add the target user as beneficiary

	/**
	 * Adds a beneficiary to the current user's list.
	 * 
	 * Steps: 1. Check if the email provided exists in the system. 2. Perform
	 * business validations (no duplicate relations, cannot add oneself). 3. Add the
	 * target user as a beneficiary if not already added.
	 * 
	 * @param currentUserId the ID of the user adding the beneficiary
	 * @param emailDto      contains the email address of the beneficiary to add
	 * @return a {@link BeneficiaryDto} representing the added beneficiary
	 * @throws EmailNotFoundException         if the email does not match any user
	 * @throws SelfRelationException          if the user tries to add themselves
	 * @throws RelationAlreadyExistsException if the beneficiary is already added
	 * @throws UserNotFoundException          if the current user does not exist
	 */
	public BeneficiaryDto addBeneficiary(int currentUserId, EmailRequestDto emailDto) throws EmailNotFoundException,
			SelfRelationException, RelationAlreadyExistsException, UserNotFoundException {
		String email = emailDto.email();
		User currentUser = userRepository.findById(currentUserId)
				.orElseThrow(() -> new UserNotFoundException(ApiMessages.USER_NOT_FOUND + currentUserId));

		User targetUser = findUserByEmail(email);

		validator.validateUserCanAddAsBeneficiary(currentUserId, targetUser);

		if (!relationAlreadyExists(currentUserId, targetUser)) {
			currentUser.addBeneficiary(targetUser);
			userRepository.save(currentUser);
		}

		return mapper.fromUserToBeneficiaryDto(targetUser);
	}

	public boolean relationAlreadyExists(int currentUserId, User targetUser) throws RelationAlreadyExistsException {
		if (userRepository.beneficiaryExists(currentUserId, targetUser.getId())) {
			throw new RelationAlreadyExistsException(ApiMessages.RELATION_ALREADY_EXISTS + targetUser.getActiveEmail());
		}
		return false;
	}

	/**
	 * Removes a beneficiary from the current user's list.
	 * 
	 * Steps: 1. Check that the current user exists. 2. Check that the beneficiary
	 * exists. 3. Verify that the beneficiary is actually in the current user's
	 * list. 4. Remove the beneficiary from the user's beneficiaries.
	 * 
	 * @param id            the ID of the current user
	 * @param beneficiaryId the ID of the beneficiary to remove
	 * @throws RelationNotFoundException if the beneficiary is not in the user's
	 *                                   list
	 * @throws UserNotFoundException     if either the current user or beneficiary
	 *                                   does not exist
	 */
	@Transactional(rollbackFor = Exception.class)
	public void removeBeneficiary(int id, int beneficiaryId) throws RelationNotFoundException, UserNotFoundException {
		User currentUser = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException(ApiMessages.USER_NOT_FOUND));
		User beneficiary = userRepository.findById(beneficiaryId)
				.orElseThrow(() -> new UserNotFoundException(ApiMessages.USER_NOT_FOUND));

		if (!userRepository.beneficiaryExists(id, beneficiaryId)) {
			throw new RelationNotFoundException(
					"User with ID " + beneficiaryId + " is not a beneficiary of user with ID " + id + ".");
		}

		if (userRepository.findBeneficiariesById(id).stream().anyMatch(b -> b.getId().equals(beneficiaryId))) {
			currentUser.removeBeneficiary(beneficiary);
			userRepository.save(currentUser);
		}
	}

	/**
	 * Permanently deletes a user by their id.
	 * <p>
	 * The user must have been previously soft-deleted (i.e., {@code deletedAt} is
	 * not null), and the soft-delete date must be older than the minimum required
	 * delay defined in {@link UserManagementSettings#getMinSoftDeleteDate()}.
	 * <p>
	 *
	 * @param id the ID of the user to be permanently deleted
	 * @throws UserNotFoundException           if no user with the given ID exists
	 * @throws UserNotSoftDeletedException     if the user has not been soft-deleted
	 * @throws UserDeletionNotAllowedException if the minimum soft-delete delay has
	 *                                         not elapsed
	 */
	public void deleteUser(int id)
			throws UserNotFoundException, UserNotSoftDeletedException, UserDeletionNotAllowedException {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException(ApiMessages.USER_NOT_FOUND));

		if (user.getDeletedAt() == null) {
			throw new UserNotSoftDeletedException(ApiMessages.USER_NOT_SOFT_DELETED);
		}

		if (user.getDeletedAt().isAfter(UserManagementSettings.getMinSoftDeleteDate())) {
			throw new UserDeletionNotAllowedException(ApiMessages.USER_DELETION_NOT_ALLOWED);
		}

		userRepository.deleteById(id);
	}

	/**
	 * Soft-deletes a user by setting their {@code deletedAt} field to the current
	 * date and time.
	 * 
	 * @param id the id of the user to soft-delete
	 * @throws UserNotFoundException  if no user with the given ID exists
	 * @throws UserAlreadySoftDeleted
	 */
	@Transactional(rollbackFor = Exception.class)
	public void softDeleteUser(int id) throws UserNotFoundException, UserAlreadySoftDeleted {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException(ApiMessages.USER_NOT_FOUND));

		if (user.getDeletedAt() != null) {
			throw new UserAlreadySoftDeleted(ApiMessages.USER_ALREADY_SOFT_DELETED);
		}

		LocalDateTime now = LocalDateTime.now();

		userRepository.softDeleteUserById(now, id);
	}

	@Transactional
	public void changePassword(int id, ChangePasswordDto changePasswordDto)
			throws UserNotFoundException, PasswordMissmatchException, SamePasswordException {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException(ApiMessages.USER_NOT_FOUND));

		if (!passwordEncoder.matches(changePasswordDto.oldPassword(), user.getPassword())) {
			throw new PasswordMissmatchException(ApiMessages.PASSWORD_MISMATCH);
		}

		if (changePasswordDto.oldPassword().equals(changePasswordDto.newPassword())) {
			throw new SamePasswordException(ApiMessages.SAME_PASSWORD);
		}

		String encodedNewPassword = passwordEncoder.encode(changePasswordDto.newPassword());

		userRepository.updatePasswordById(id, encodedNewPassword);
	}

	/**
	 * ADMIN ONLY Updates the password of a user by their id. This method is
	 * intended for administrative purposes only and does not require the old
	 * password for verification.
	 *
	 * @param id          the ID of the user whose password is to be changed
	 * @param newPassword the new plain text password to set for the user
	 * @throws UserNotFoundException if no user exists with the provided ID
	 */
	@Transactional(rollbackFor = Exception.class)
	public void changePasswordAdminOnly(int id, ChangePasswordDto newPasswordDto) throws UserNotFoundException {
		userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(ApiMessages.USER_NOT_FOUND));

		String encodedPassword = passwordEncoder.encode(newPasswordDto.newPassword());

		userRepository.updatePasswordById(id, encodedPassword);
	}

	@Transactional(rollbackFor = Exception.class)
	public void changeEmail(int id, ChangeEmailDto changeEmailDto) throws UserNotFoundException, EmailNotFoundException, SameEmailException, ForbiddenAccessException {
		userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(ApiMessages.USER_NOT_FOUND));
		String currentEmail = changeEmailDto.oldEmail();
		
		User userByActiveEmail = userRepository.findByActiveEmail(currentEmail).orElseThrow(() -> new EmailNotFoundException(ApiMessages.EMAIL_NOT_FOUND));
		
		if (userByActiveEmail.getId() != id) {
			throw new ForbiddenAccessException(currentEmail);
		}
		
		if (changeEmailDto.newEmail().equals(currentEmail)) {
			throw new SameEmailException(ApiMessages.SAME_EMAIL);
		}
		
		userRepository.updateEmail(id, changeEmailDto.newEmail());
	}
}
