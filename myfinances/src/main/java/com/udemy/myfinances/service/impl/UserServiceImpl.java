package com.udemy.myfinances.service.impl;

import com.udemy.myfinances.exception.AuthException;
import com.udemy.myfinances.exception.BusinessLogicException;
import com.udemy.myfinances.model.entity.User;
import com.udemy.myfinances.model.repository.UserRepository;
import com.udemy.myfinances.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository repository;

	public UserServiceImpl(UserRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public User authenticate(String email, String password) {
		Optional<User> user = repository.findByEmail(email);

		if (!user.isPresent()) {
			throw new AuthException("User not found for the given email.");
		}

		if (!user.get().getPassword().equals(password)) {
			throw new AuthException("Invalid password.");
		}

		return user.get();
	}

	@Override
	@Transactional
	public User save(User user) {
		validateEmail(user.getEmail());
		return repository.save(user);
	}

	@Override
	public void validateEmail(String email) {
		boolean existsEmail = repository.existsByEmail(email);

		if (existsEmail) {
			throw new BusinessLogicException("There is already a registered user with this email.");
		}
	}

	@Override
	public Optional<User> findById(Long id) {
		return repository.findById(id);
	}

}
