package com.widebroadcast.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CreateSlotRequestDto {
	
	@NotNull
	private String programmeName;
	@NotNull
	private LocalDateTime slotFromDateTime;
	@NotNull
	private LocalDateTime slotToDateTime;
	@NotNull
	private Integer planTypeId;

}
