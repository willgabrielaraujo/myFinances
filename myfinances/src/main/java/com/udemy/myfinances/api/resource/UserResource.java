package com.udemy.myfinances.api.resource;

import com.udemy.myfinances.api.dto.UserDto;
import com.udemy.myfinances.exception.AuthException;
import com.udemy.myfinances.exception.BusinessLogicException;
import com.udemy.myfinances.model.entity.User;
import com.udemy.myfinances.service.EntryService;
import com.udemy.myfinances.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserResource {
	private final UserService service;
	private final EntryService entryService;

	@PostMapping("/authenticate")
	public ResponseEntity authenticate(@RequestBody UserDto dto) {
		try {
			User authenticatedUser = service.authenticate(dto.getEmail(), dto.getPassword());
			return ResponseEntity.ok(authenticatedUser);
		} catch (AuthException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping
	public ResponseEntity save(@RequestBody UserDto dto) {
		User user = User.builder().name(dto.getName()).email(dto.getEmail()).password(dto.getPassword()).build();

		try {
			User savedUser = service.save(user);
			return new ResponseEntity(savedUser, HttpStatus.CREATED);
		} catch (BusinessLogicException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("{id}/balance")
	public ResponseEntity getBalance(@PathVariable("id") Long userId) {
		Optional<User> user = service.findById(userId);

		if (!user.isPresent()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}

		BigDecimal balance = entryService.getBalanceByUser(userId);
		return ResponseEntity.ok(balance);
	}
}
