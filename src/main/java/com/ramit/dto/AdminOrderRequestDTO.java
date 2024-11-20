package com.ramit.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AdminOrderRequestDTO {
	String email;
	LocalDateTime startDate;
	LocalDateTime endDate;
}
