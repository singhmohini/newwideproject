package com.widebroadcast.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class BookSlotRequestDto {
	
	@NotNull
	private List<TimeSlotRequestDto> timeSlots;

}
