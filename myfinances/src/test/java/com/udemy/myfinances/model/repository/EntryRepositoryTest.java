package com.udemy.myfinances.model.repository;

import com.udemy.myfinances.model.entity.Entry;
import com.udemy.myfinances.model.enums.EntryStatus;
import com.udemy.myfinances.model.enums.EntryType;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class EntryRepositoryTest {
	@Autowired
	EntryRepository repository;

	@Autowired
	EntityManager entityManager;

	@Test
	public void itMustSaveAnEntry() {
		// Scenario
		Entry entry = createEntry();

		// Execution
		Entry savedEntry = repository.save(entry);

		// Verification
		Assertions.assertThat(savedEntry.getId()).isNotNull();
	}

	@Test
	public void itMustDeleteAnEntry() {
		// Scenario
		Entry entry = createAndPesistsAnEntry();
		Entry savedEntry = entityManager.find(Entry.class, entry.getId());

		// Execution
		repository.delete(savedEntry);

		// Verification
		Entry entryNotFound = entityManager.find(Entry.class, savedEntry.getId());
		Assertions.assertThat(entryNotFound).isNull();
	}

	@Test
	public void itMustUpdateAnEntry() {
		// Scenario
		Integer year  = 2020;
		String description = "Updating test";
		EntryStatus status = EntryStatus.CANCELED;

		Entry entry = createAndPesistsAnEntry();
		entry.setYear(year);
		entry.setDescription(description);
		entry.setStatus(status);
		repository.save(entry);

		// Execution
		Entry updatedEntry = entityManager.find(Entry.class, entry.getId());

		// Verification
		Assertions.assertThat(updatedEntry.getYear()).isEqualTo(year);
		Assertions.assertThat(updatedEntry.getDescription()).isEqualTo(description);
		Assertions.assertThat(updatedEntry.getStatus()).isEqualTo(status);
	}

	@Test
	public void itMustFindAnEntryById() {
		// Scenario
		Entry entry = createAndPesistsAnEntry();

		// Execution
		Optional<Entry> entryFound = repository.findById(entry.getId());

		// Verification
		Assertions.assertThat(entryFound.isPresent()).isTrue();
	}

	private Entry createAndPesistsAnEntry() {
		Entry entry = createEntry();
		entityManager.persist(entry);
		return entry;
	}

	public static Entry createEntry() {
		return Entry.builder().year(2020).month(1).description("Any entry").amount(BigDecimal.valueOf(10))
				.type(EntryType.REVENUE).status(EntryStatus.PENDING).registrationDate(LocalDate.now()).build();
	}
}
