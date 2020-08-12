package com.udemy.myfinances.service;

import com.udemy.myfinances.model.entity.User;

import java.util.Optional;

public interface UserService {
	User authenticate(String email, String password);

	User save(User user);

	void validateEmail(String email);

	Optional<User> findById(Long id);
}
