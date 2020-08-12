package com.udemy.myfinances.service;

import com.udemy.myfinances.exception.BusinessLogicException;
import com.udemy.myfinances.model.entity.Entry;
import com.udemy.myfinances.model.entity.User;
import com.udemy.myfinances.model.enums.EntryStatus;
import com.udemy.myfinances.model.repository.EntryRepository;
import com.udemy.myfinances.model.repository.EntryRepositoryTest;
import com.udemy.myfinances.service.impl.EntryServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class EntryServiceTest {
	@SpyBean
	EntryServiceImpl service;

	@MockBean
	EntryRepository repository;

	@Test
	public void itMustSaveAnEntry() {
		// Scenario
		Entry newEntry = EntryRepositoryTest.createEntry();
		Mockito.doNothing().when(service).validate(newEntry);

		Entry savenEntry = EntryRepositoryTest.createEntry();
		savenEntry.setId(1L);
		savenEntry.setStatus(EntryStatus.PENDING);
		Mockito.when(repository.save(newEntry)).thenReturn(savenEntry);

		// Execution
		Entry entry = service.save(newEntry);

		// Verification
		Assertions.assertThat(entry.getId()).isEqualTo(savenEntry.getId());
		Assertions.assertThat(entry.getStatus()).isEqualTo(EntryStatus.PENDING);
	}

	@Test
	public void itMustNotSaveAnEntryWhenThereIsAValidationError() {
		// Scenario
		Entry entry = EntryRepositoryTest.createEntry();
		Mockito.doThrow(BusinessLogicException.class).when(service).validate(entry);

		// Execution and Validation
		Assertions.catchThrowableOfType(() -> service.save(entry), BusinessLogicException.class);
		Mockito.verify(repository, Mockito.never()).save(entry);
	}

	@Test
	public void itMustUpdateAnEntry() {
		// Scenario
		Entry entry = EntryRepositoryTest.createEntry();
		entry.setId(1L);
		entry.setStatus(EntryStatus.PENDING);

		Mockito.doNothing().when(service).validate(entry);
		Mockito.when(repository.save(entry)).thenReturn(entry);

		// Execution
		service.update(entry);

		// Verification
		Mockito.verify(repository, Mockito.times(1)).save(entry);
	}

	@Test
	public void itMustThrowAnExceptionWhenTryingUpdateAnEntryNotFound() {
		// Scenario
		Entry entry = EntryRepositoryTest.createEntry();

		// Execution and Verification
		Assertions.catchThrowableOfType(() -> service.update(entry), NullPointerException.class);
		Mockito.verify(repository, Mockito.never()).save(entry);
	}

	@Test
	public void itMustNotUpdateAnEntryWhenThereIsAValidationError() {
		// Scenario
		Entry entry = EntryRepositoryTest.createEntry();
		entry.setId(1L);
		Mockito.doThrow(BusinessLogicException.class).when(service).validate(entry);

		// Execution and Verification
		Assertions.catchThrowableOfType(() -> service.update(entry), BusinessLogicException.class);
		Mockito.verify(repository, Mockito.never()).save(entry);
	}

	@Test
	public void itMustDeleteAnEntry() {
		// Scenario
		Entry entry = EntryRepositoryTest.createEntry();
		entry.setId(1L);

		// Execution
		service.delete(entry);

		// Verification
		Mockito.verify(repository).delete(entry);
	}

	@Test
	public void itMustThrowAnExceptionWhenTryingDeleteAnUnsavedEntry() {
		// Scenario
		Entry entry = EntryRepositoryTest.createEntry();

		// Execution
		Assertions.catchThrowableOfType(() -> service.delete(entry), NullPointerException.class);

		// Verification
		Mockito.verify(repository, Mockito.never()).delete(entry);
	}

	@Test
	public void itMustFilterEntries() {
		// Scenario
		Entry entry = EntryRepositoryTest.createEntry();
		entry.setId(1L);

		List<Entry> entries = Arrays.asList(entry);
		Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(entries);

		// Execution
		List<Entry> result = service.search(entry);

		// Verification
		Assertions.assertThat(result).isNotEmpty().hasSize(1).contains(entry);
	}

	@Test
	public void itMustUpdateTheStatusEntry() {
		// Scenario
		Entry entry = EntryRepositoryTest.createEntry();
		entry.setId(1L);
		entry.setStatus(EntryStatus.PENDING);

		EntryStatus newEntry = EntryStatus.MADE;
		Mockito.doReturn(entry).when(service).update(entry);

		// Execution
		service.updateStatus(entry, newEntry);

		// Verification
		Assertions.assertThat(entry.getStatus()).isEqualTo(newEntry);
		Mockito.verify(service).update(entry);
	}

	@Test
	public void itMustFindEntryById() {
		// Scenario
		Entry entry = EntryRepositoryTest.createEntry();
		Long id = 1L;
		entry.setId(id);

		Mockito.when(repository.findById(id)).thenReturn(Optional.of(entry));

		// Execution
		Optional<Entry> result = service.findById(id);

		// Verification
		Assertions.assertThat(result.isPresent()).isTrue();
	}

	@Test
	public void itMustReturnEmptyWhenThereIsNoEntry() {
		// Scenario
		Entry entry = EntryRepositoryTest.createEntry();
		Long id = 1L;
		entry.setId(id);

		Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

		// Execution
		Optional<Entry> result = service.findById(id);

		// Verification
		Assertions.assertThat(result.isPresent()).isFalse();
	}

	@Test
	public void itMustThrowAnExceptionWhenValidateAnEntry() {
		Entry entry = new Entry();
		Throwable error = Assertions.catchThrowable(() -> service.validate(entry));
		Assertions.assertThat(error).isInstanceOf(BusinessLogicException.class).hasMessage("Enter a valid description.");

		entry.setDescription("");
		error = Assertions.catchThrowable(() -> service.validate(entry));
		Assertions.assertThat(error).isInstanceOf(BusinessLogicException.class).hasMessage("Enter a valid description.");

		entry.setDescription("test");
		error = Assertions.catchThrowable(() -> service.validate(entry));
		Assertions.assertThat(error).isInstanceOf(BusinessLogicException.class).hasMessage("Enter a valid month.");

		entry.setMonth(0);
		error = Assertions.catchThrowable(() -> service.validate(entry));
		Assertions.assertThat(error).isInstanceOf(BusinessLogicException.class).hasMessage("Enter a valid month.");

		entry.setMonth(13);
		error = Assertions.catchThrowable(()-> service.validate(entry));
		Assertions.assertThat(error).isInstanceOf(BusinessLogicException.class).hasMessage("Enter a valid month.");

		entry.setMonth(1);
		error = Assertions.catchThrowable(()-> service.validate(entry));
		Assertions.assertThat(error).isInstanceOf(BusinessLogicException.class).hasMessage("Enter a valid year.");

		entry.setYear(101);
		error = Assertions.catchThrowable(()-> service.validate(entry));
		Assertions.assertThat(error).isInstanceOf(BusinessLogicException.class).hasMessage("Enter a valid year.");

		entry.setYear(2020);
		error = Assertions.catchThrowable(() -> service.validate(entry));
		Assertions.assertThat(error).isInstanceOf(BusinessLogicException.class).hasMessage("Enter a valid user.");

		entry.setUser(new User());
		error = Assertions.catchThrowable(() -> service.validate(entry));
		Assertions.assertThat(error).isInstanceOf(BusinessLogicException.class).hasMessage("Enter a valid user.");

		entry.getUser().setId(1L);
		error = Assertions.catchThrowable(() -> service.validate(entry));
		Assertions.assertThat(error).isInstanceOf(BusinessLogicException.class).hasMessage("Enter a valid amount.");

		entry.setAmount(BigDecimal.ZERO);
		error = Assertions.catchThrowable(() -> service.validate(entry));
		Assertions.assertThat(error).isInstanceOf(BusinessLogicException.class).hasMessage("Enter a valid amount.");

		entry.setAmount(BigDecimal.valueOf(1));
		error = Assertions.catchThrowable(() -> service.validate(entry));
		Assertions.assertThat(error).isInstanceOf(BusinessLogicException.class).hasMessage("Enter a valid type.");
	}
}
