package com.udemy.myfinances.model.repository;

import com.udemy.myfinances.model.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserRepositoryTest {

	@Autowired
	UserRepository repository;

	@Autowired
	TestEntityManager entityManager;

	@Test
	public void itMustCheckTheExistenceOfARegisteredUserWithAnEmail() {
		// Scenario
		User user = createUser();
		entityManager.persist(user);

		// Execution
		boolean result = repository.existsByEmail("user@email.com");

		// Verification
		Assertions.assertThat(result).isTrue();
	}

	@Test
	public void itMustReturnFalseWhenThereIsNoRegisteredUserWithTheGivenEmail() {
		// Cenario
		// Execution
		boolean result = repository.existsByEmail("user@email.com");

		// Verification
		Assertions.assertThat(result).isFalse();

	}

	@Test
	public void itMustPersistAUserInTheDatabase() {
		// Scenario
		User user = createUser();

		// Execution
		User savedUser = repository.save(user);

		// Verification
		Assertions.assertThat(savedUser.getId()).isNotNull();
	}

	@Test
	public void itMustSearchingForAUserByEmail() {
		// Scenario
		User user = createUser();
		entityManager.persist(user);

		// Execution
		Optional<User> result = repository.findByEmail("user@email.com");

		// Verification
		Assertions.assertThat(result.isPresent()).isTrue();
	}

	@Test
	public void itMustReturnEmptyWhenSearchingForUserByEmailWhenThereIsNoUserInTheDatabase() {

		// Scenario
		// Execution
		Optional<User> result = repository.findByEmail("user@email.com");

		// Verification
		Assertions.assertThat(result.isPresent()).isFalse();

	}

	private User createUser() {
		return User.builder().name("User").email("user@email.com").build();
	}

}
