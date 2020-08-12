package com.udemy.myfinances.model.entity;

import com.udemy.myfinances.model.enums.EntryStatus;
import com.udemy.myfinances.model.enums.EntryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "entry", schema = "finances")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Entry {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String description;

	private Integer month;

	private Integer year;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	private BigDecimal amount;

	@Column(name = "registration_date")
	@Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
	private LocalDate registrationDate;

	@Enumerated(value = EnumType.STRING)
	private EntryType type;

	@Enumerated(value = EnumType.STRING)
	private EntryStatus status;
}
