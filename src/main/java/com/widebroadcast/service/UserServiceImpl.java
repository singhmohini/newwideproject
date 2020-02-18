package com.widebroadcast.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.widebroadcast.common.WideBroadCastEnum.TimeSlotStatus;
import com.widebroadcast.constant.AppConstant;
import com.widebroadcast.dto.BookSlotRequestDto;
import com.widebroadcast.dto.LoginRequestDto;
import com.widebroadcast.dto.LoginResponseDto;
import com.widebroadcast.dto.ResponseDto;
import com.widebroadcast.entity.TimeSlot;
import com.widebroadcast.entity.User;
import com.widebroadcast.exception.InvalidLoginCredentialException;
import com.widebroadcast.exception.SlotNotAvailableException;
import com.widebroadcast.exception.UserNotFoundException;
import com.widebroadcast.repository.TimeSlotRepository;
import com.widebroadcast.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * UserServiceImpl Class - We are using this class for user activities of user
 * login purpose. As a user can login with phone number and password. A login
 * can access for admin and sales manager roles.
 * 
 * @author Govindasamy.C
 * @since 17-02-2020
 * @version V1.1
 *
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	TimeSlotRepository timeSlotRepository;


	/**
	 * As a user can login with phone number and password for login purpose. A login
	 * can access for admin and sales manager roles.
	 * 
	 * @param loginRequestDto - details of the username and password.
	 * @return - return the values of the userId, name and role for user.
	 * @author Govindasamy.C
	 * @throws UserNotFoundException
	 * @since 17-02-2020
	 */
	@Override
	public LoginResponseDto userLogin(LoginRequestDto loginRequestDto) throws InvalidLoginCredentialException {
		log.info("user login based on the user name and password...");
		Optional<User> user = userRepository.findByPhoneNumberAndPassword(loginRequestDto.getPhoneNumber(),
				loginRequestDto.getPassword());
		if (!user.isPresent()) {
			throw new InvalidLoginCredentialException(AppConstant.INVALID_LOGIN_CREDENTCIAL);
		}
		log.info("setting the responsse details of the user login...");
		LoginResponseDto loginResponseDto = new LoginResponseDto();
		BeanUtils.copyProperties(user.get(), loginResponseDto);
		log.info("return the response details in user login...");
		return loginResponseDto;
	}

	/**
	 * As a slaes person can book the slots between the available timeline
	 *
	 * @param createSlotRequestDto - contains fromDateTime, toDateTime, planId, and
	 *                             programName.
	 * @return - success message and status code with Http response
	 * @author Raghu M
	 * @throws SlotNotAvailableException
	 * @since 17-02-2020
	 */
	@Override
	@Transactional
	public ResponseDto bookSlots(Integer userId, BookSlotRequestDto bookSlotRequestDto)
			throws SlotNotAvailableException {
		log.info("UserServiceImpl bookSlots ---> booking slot");
		HashSet<TimeSlot> timeSlots = new HashSet<>();
		int[] expectedCount  = {0};
		bookSlotRequestDto.getTimeSlots().forEach(timeSlot -> {
			List<TimeSlot> tempTimeSlots = new ArrayList<>();
			tempTimeSlots.addAll(timeSlotRepository.findByStatusAndSlotDateTimeBetween(TimeSlotStatus.AVAILABLE,
					timeSlot.getFromDateTime(), timeSlot.getToDateTime()));
			tempTimeSlots.forEach(temp -> temp.setCustomerName(timeSlot.getCustomerName()));
			timeSlots.addAll(tempTimeSlots);
			while (timeSlot.getFromDateTime().isBefore(timeSlot.getToDateTime().plusSeconds(1))) {
				expectedCount[0]++;
				timeSlot.setFromDateTime(timeSlot.getFromDateTime().plusSeconds(1));
			}
		});
		Integer count = timeSlots.size();
		System.out.println(count);
		System.out.println(expectedCount[0]);
		if (expectedCount[0] != count) {
			log.error("UserServiceImpl bookSlots ---> SlotNotAvailableException occured");
			throw new SlotNotAvailableException(AppConstant.SLOT_NOT_AVAILABLE);
		}
		timeSlots.forEach(timeSlot -> {
			timeSlot.setStatus(TimeSlotStatus.BOOKED);
			User user = new User();
			user.setUserId(userId);
			timeSlot.setUser(user);
		});
		timeSlotRepository.saveAll(timeSlots);
		log.info("UserServiceImpl bookSlots ---> slots booked");
		return new ResponseDto();
	}

}
