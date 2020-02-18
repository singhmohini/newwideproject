package com.widebroadcast.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class TimeSlotRequestDto {
	
	@NotNull
	private LocalDateTime fromDateTime;
	@NotNull
	private LocalDateTime toDateTime;
	@NotNull
	private String customerName;

}
