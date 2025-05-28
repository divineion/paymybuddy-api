package com.paymybuddy.api.services.transfer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.paymybuddy.api.model.Transfer;
import com.paymybuddy.api.model.User;
import com.paymybuddy.api.services.dto.TransferDto;

@ExtendWith(MockitoExtension.class)
public class TransferMapperTest {
	private final TransferMapper mapper = new TransferMapper();
	
	@Test
	public void testFromTransferToTransferDto_WhenUserIsSender_shouldReturnDtoWithTotalAmount() {
		// GIVEN
		User sender = mock(User.class);
		when(sender.getId()).thenReturn(1);
		
		User receiver = mock(User.class);
		when(receiver.getId()).thenReturn(2);
		
		Transfer transfer = mock(Transfer.class);
		when(transfer.getSender()).thenReturn(sender);
		when(transfer.getReceiver()).thenReturn(receiver);
		when(transfer.getTotalAmount()).thenReturn(new BigDecimal("14.426"));
		
		//WHEN
		TransferDto dto = mapper.fromTransferToTransferDto(transfer, 1);
		
		BigDecimal expectedAmount = new BigDecimal("14.43");
		
		//THEN
		assertEquals(expectedAmount, dto.amount());
	}
	
	@Test
	public void testFromTransferToTransferDto_WhenUserIsReceiver_shouldReturnDtoWithAmountExcludingFees() {
		// GIVEN	
		User sender = mock(User.class);
		when(sender.getId()).thenReturn(1);
		when(sender.getUsername()).thenReturn("John");
		
		User receiver = mock(User.class);
		when(receiver.getId()).thenReturn(2);
		when(receiver.getUsername()).thenReturn("Jack");
		
		Transfer transfer = mock(Transfer.class);
		when(transfer.getSender()).thenReturn(sender);
		when(transfer.getReceiver()).thenReturn(receiver);
		when(transfer.getAmount()).thenReturn(new BigDecimal("50"));
		
		//WHEN
		TransferDto dto = mapper.fromTransferToTransferDto(transfer, 2);
		
		BigDecimal expectedAmount = new BigDecimal("50.00");
		
		//THEN
		assertEquals(expectedAmount, dto.amount());
	}
}
