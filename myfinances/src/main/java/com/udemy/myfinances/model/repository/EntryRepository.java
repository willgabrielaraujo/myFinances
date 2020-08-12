package com.udemy.myfinances.model.repository;

import com.udemy.myfinances.model.entity.Entry;
import com.udemy.myfinances.model.enums.EntryStatus;
import com.udemy.myfinances.model.enums.EntryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface EntryRepository extends JpaRepository<Entry, Long> {
	@Query("select sum(e.amount) from Entry e join e.user u where u.id = :userId and e.type = :type and e.status = :status group by u")
	BigDecimal getBalanceByEntryTypeAndUserAndEntryStatus(@Param("userId") Long userId,
														  @Param("type") EntryType type,
														  @Param("status") EntryStatus status);
}
