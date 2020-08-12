package com.udemy.myfinances.service.impl;

import com.udemy.myfinances.exception.BusinessLogicException;
import com.udemy.myfinances.model.entity.Entry;
import com.udemy.myfinances.model.enums.EntryStatus;
import com.udemy.myfinances.model.enums.EntryType;
import com.udemy.myfinances.model.repository.EntryRepository;
import com.udemy.myfinances.service.EntryService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EntryServiceImpl implements EntryService {
	private final EntryRepository repository;

	public EntryServiceImpl(EntryRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	@Transactional
	public Entry save(Entry entry) {
		validate(entry);
		entry.setStatus(EntryStatus.PENDING);

		return repository.save(entry);
	}

	@Override
	@Transactional
	public Entry update(Entry entry) {
		Objects.requireNonNull(entry.getId());
		validate(entry);

		return repository.save(entry);
	}

	@Override
	@Transactional
	public void delete(Entry entry) {
		Objects.requireNonNull(entry.getId());
		repository.delete(entry);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Entry> search(Entry entryFilter) {
		Example example = Example.of(entryFilter,
				ExampleMatcher.matching().withIgnoreCase().withStringMatcher(StringMatcher.CONTAINING));
		return repository.findAll(example);
	}

	@Override
	@Transactional
	public Entry updateStatus(Entry entry, EntryStatus entryStatus) {
		entry.setStatus(entryStatus);
		return update(entry);
	}

	@Override
	public void validate(Entry entry) {
		if (entry.getDescription() == null || entry.getDescription().trim().equals("")) {
			throw new BusinessLogicException("Enter a valid description.");
		}

		if (entry.getMonth() == null || entry.getMonth() < 1 || entry.getMonth() > 12) {
			throw new BusinessLogicException("Enter a valid month.");
		}

		if (entry.getYear() == null || entry.getYear().toString().length() != 4) {
			throw new BusinessLogicException("Enter a valid year.");
		}

		if (entry.getUser() == null || entry.getUser().getId() == null) {
			throw new BusinessLogicException("Enter a valid user.");
		}

		if (entry.getAmount() == null || entry.getAmount().compareTo(BigDecimal.ZERO) < 1) {
			throw new BusinessLogicException("Enter a valid amount.");
		}

		if (entry.getType() == null) {
			throw new BusinessLogicException("Enter a valid type.");
		}
	}

	@Override
	public Optional<Entry> findById(Long id) {
		return repository.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal getBalanceByUser(Long userId) {
		BigDecimal revenues = repository.getBalanceByEntryTypeAndUserAndEntryStatus(userId, EntryType.REVENUE, EntryStatus.MADE);
		BigDecimal expenses = repository.getBalanceByEntryTypeAndUserAndEntryStatus(userId, EntryType.EXPENSE, EntryStatus.MADE);

		if (revenues == null) {
			revenues = BigDecimal.ZERO;
		}

		if (expenses == null) {
			expenses = BigDecimal.ZERO;
		}

		return revenues.subtract(expenses);
	}

}
