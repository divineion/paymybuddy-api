package com.paymybuddy.api.services.unit;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.paymybuddy.api.exceptions.UserNotFoundException;
import com.paymybuddy.api.model.User;
import com.paymybuddy.api.repositories.UserRepository;
import com.paymybuddy.api.services.dto.TransferRequestDto;
import com.paymybuddy.api.services.transfer.TransferService;

@ExtendWith(MockitoExtension.class)
public class TransferServiceTest {
	@Mock
	UserRepository userRepository;

	@InjectMocks
	TransferService service;

	@Test
	public void testCreateTransfer_ShouldThrowNotFound() {
		int senderId = 1;
		int receiverId = 2;
		User sender = User.referenceOnly(senderId, "senderName", "sender@email.com", new BigDecimal("2000"));
		String description = "Short description for a transfer";
		BigDecimal amount = new BigDecimal("100");
		TransferRequestDto transferReqDto = new TransferRequestDto(1, 2, description, amount);
		
		when(userRepository.findById(senderId)).thenReturn(Optional.of(sender));
		when(userRepository.findById(receiverId)).thenReturn(Optional.empty());
		
		Assertions.assertThrows( UserNotFoundException.class, () -> { 
			service.createTransfer(transferReqDto);
		});
	}
}
