package com.paymybuddy.api.services.user;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.paymybuddy.api.exceptions.UserDeletionNotAllowedException;
import com.paymybuddy.api.exceptions.UserNotFoundException;
import com.paymybuddy.api.exceptions.UserNotSoftDeletedException;
import com.paymybuddy.api.model.User;
import com.paymybuddy.api.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	@Mock
	private UserRepository repository;
	
	@InjectMocks
	private UserService service;
	
	@Test
	public void testDeleteUser() throws Exception {
		User user = mock(User.class);
		int id = 1;
		
		when(repository.findById(id)).thenReturn(Optional.of(user));
		when(user.getDeletedAt()).thenReturn(LocalDateTime.now().minusMonths(7));
				
		service.deleteUser(id);
		
		verify(repository, times(1)).findById(id);
		verify(repository, times(1)).deleteById(id);		
	}
	
	@Test
	public void testDeleteUser_ShouldThrowUserNotFound() throws Exception {
		int id = 1;
		
		when(repository.findById(id)).thenReturn(Optional.empty());
		
		Assertions.assertThrows(UserNotFoundException.class, () -> {
			service.deleteUser(id);
		});
	}

	
	@Test
	public void testDeleteUser_ShouldThrowUserNotSoftDeletedException() throws Exception {
		User user = mock(User.class);
		int id = 1;
		
		when(repository.findById(id)).thenReturn(Optional.of(user));
		when(user.getDeletedAt()).thenReturn(null);
				
		Assertions.assertThrows(UserNotSoftDeletedException.class, () -> {
			service.deleteUser(id);
		});
	}
	
	@Test
	public void testDeleteUser_ShouldThrowUserDeletionNotAllowedException() throws Exception {
		User user = mock(User.class);
		int id = 1;
		LocalDateTime deletedAtDate = LocalDateTime.now().minusMonths(5);
		
		when(repository.findById(id)).thenReturn(Optional.of(user));
		when(user.getDeletedAt()).thenReturn(deletedAtDate);
		
		Assertions.assertThrows(UserDeletionNotAllowedException.class, () -> {
			service.deleteUser(id);
		});
	}
}
