package com.udemy.myfinances.service;

import com.udemy.myfinances.model.entity.Entry;
import com.udemy.myfinances.model.enums.EntryStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface EntryService {

	Entry save(Entry entry);

	Entry update(Entry entry);

	void delete(Entry entry);

	List<Entry> search(Entry entryFilter);

	Entry updateStatus(Entry entry, EntryStatus entryStatus);

	void validate(Entry entry);

	Optional<Entry> findById(Long id);

	BigDecimal getBalanceByUser(Long userId);
}
