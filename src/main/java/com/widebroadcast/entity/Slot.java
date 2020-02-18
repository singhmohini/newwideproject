package com.widebroadcast.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Version;

import lombok.Data;

@Data
@Entity
public class Slot {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer slotId;
	private String programName;

	@ManyToOne
	@JoinColumn(name="plan_type_id")
	private PlanType plantype;

	private String status;
	
	@OneToMany(fetch = FetchType.LAZY, cascade =  CascadeType.ALL, mappedBy = "slot")
	@OrderBy("id ASC")
    private List<TimeSlot> timeSlots;

}
