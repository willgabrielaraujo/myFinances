package com.udemy.myfinances.api.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntryDto {
	private Long id;
	private String description;
	private Integer month;
	private Integer year;
	private BigDecimal amount;
	private Long user;
	private String type;
	private String status;
}
