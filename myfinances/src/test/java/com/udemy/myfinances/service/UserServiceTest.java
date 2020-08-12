package com.udemy.myfinances.service;

import com.udemy.myfinances.exception.AuthException;
import com.udemy.myfinances.exception.BusinessLogicException;
import com.udemy.myfinances.model.entity.User;
import com.udemy.myfinances.model.repository.UserRepository;
import com.udemy.myfinances.service.impl.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UserServiceTest {

	@SpyBean
	UserServiceImpl service;

	@MockBean
	UserRepository repository;

	@Test(expected = Test.None.class)
	public void itMustSaveAUser() {
		// Scenario
		String name = "User";
		String email = "user@email.com";
		String password = "password123";

		Mockito.doNothing().when(service).validateEmail(Mockito.anyString());
		User user = User.builder().id(1L).name(name).email(email).password(password).build();
		Mockito.when(repository.save(Mockito.any(User.class))).thenReturn(user);

		// Execution
		User savedUser = service.save(new User());

		// Verification
		Assertions.assertThat(savedUser).isNotNull();
		Assertions.assertThat(savedUser.getId()).isEqualTo(1L);
		Assertions.assertThat(user.getName()).isEqualTo(name);
		Assertions.assertThat(user.getEmail()).isEqualTo(email);
		Assertions.assertThat(user.getPassword()).isEqualTo(password);

	}

	@Test(expected = BusinessLogicException.class)
	public void itMustNotSaveAUserWithAnEmailThatAlreadyExistsInTheDatabase() {
		// Scenario
		String email = "user@email.com";

		User user = User.builder().email(email).build();
		Mockito.doThrow(BusinessLogicException.class).when(service).validateEmail(email);

		// Execution
		service.save(user);

		// Verification
		Mockito.verify(repository, Mockito.never()).save(user);

	}

	@Test(expected = Test.None.class)
	public void itMustAuthenticateAUser() {
		// Scenario
		String email = "user@email.com";
		String password = "password123";

		User user = User.builder().id(1L).email(email).password(password).build();
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(user));

		// Execution
		User result = service.authenticate(email, password);

		// Verification
		Assertions.assertThat(result).isNotNull();
	}

	public void itMustThrowAnExceptionWhenAUserWithAnEmailIsNotFound() {
		// Scenario
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

		// Execution
		Throwable exception = Assertions.catchThrowable(() -> service.authenticate("user@email.com", "password"));

		// Verification
		Assertions.assertThat(exception).isInstanceOf(AuthException.class)
				.hasMessage("User not found for the given email.");
	}

	public void itMustThrowAnExceptionWhenInvalidPassword() {
		// Scenario
		String email = "user@email.com";

		User user = User.builder().id(1L).email(email).password("password123").build();
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));

		// Execution
		Throwable exception = Assertions.catchThrowable(() -> service.authenticate(email, "password321"));

		// Verification
		Assertions.assertThat(exception).isInstanceOf(AuthException.class).hasMessage("Invalid password.");
	}

	@Test(expected = Test.None.class)
	public void itMustValidateAnEmail() {
		// Scenario
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);

		// Execution
		service.validateEmail("email@email.com");
	}

	@Test(expected = BusinessLogicException.class)
	public void itMustThrowAnExceptionWhenValidatingAnEmailWhenThereIsAUserWithARegisteredEmail() {
		// Scenario
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);

		// Execution
		service.validateEmail("user@email.com");
	}
}
