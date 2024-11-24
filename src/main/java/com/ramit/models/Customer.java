package com.ramit.models;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {
	@Id
	Integer customerId;
	String name;
	String email;
	String phoneNumber;
	Address address;
}
