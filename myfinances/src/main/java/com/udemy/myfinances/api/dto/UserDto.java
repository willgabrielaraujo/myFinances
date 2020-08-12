package com.udemy.myfinances.api.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
	public String name;
	public String email;
	public String password;
}
