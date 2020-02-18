package com.widebroadcast.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.widebroadcast.dto.BookSlotRequestDto;
import com.widebroadcast.dto.TimeSlotRequestDto;
import com.widebroadcast.exception.SlotNotAvailableException;
import com.widebroadcast.service.UserService;

@RestController
@RequestMapping("/test")
public class CuncurrentController {

	@Autowired
	UserService userService;
	
	@GetMapping
	public String testCuncurrency() {
		
		Thread th1 = new Thread(new Thread1());
		Thread th2 = new Thread(new Thread1());
		th1.start();
		th2.start();
		return "success";
		
	}
	
	class Thread1 implements Runnable{

		@Override
		public void run() {
			TimeSlotRequestDto timeSlotRequestDto = new TimeSlotRequestDto();
			List<TimeSlotRequestDto> timeSlotRequestDtos = new ArrayList<>();
			timeSlotRequestDto.setCustomerName("test");
			timeSlotRequestDto.setFromDateTime(LocalDateTime.parse("2020-02-18T01:00:00"));
			timeSlotRequestDto.setToDateTime(LocalDateTime.parse("2020-02-18T01:00:00"));
			timeSlotRequestDtos.add(timeSlotRequestDto);
			BookSlotRequestDto bookSlotRequestDto = new BookSlotRequestDto();
			bookSlotRequestDto.setTimeSlots(timeSlotRequestDtos);
			try {
				userService.bookSlots(2, bookSlotRequestDto);
			} catch (SlotNotAvailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	class Thread2 implements Runnable{

		@Override
		public void run() {
			TimeSlotRequestDto timeSlotRequestDto = new TimeSlotRequestDto();
			List<TimeSlotRequestDto> timeSlotRequestDtos = new ArrayList<>();
			timeSlotRequestDto.setCustomerName("test");
			timeSlotRequestDto.setFromDateTime(LocalDateTime.parse("2020-02-18T01:00:00"));
			timeSlotRequestDto.setToDateTime(LocalDateTime.parse("2020-02-18T01:00:00"));
			timeSlotRequestDtos.add(timeSlotRequestDto);
			BookSlotRequestDto bookSlotRequestDto = new BookSlotRequestDto();
			bookSlotRequestDto.setTimeSlots(timeSlotRequestDtos);
			try {
				userService.bookSlots(2, bookSlotRequestDto);
			} catch (SlotNotAvailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

}
