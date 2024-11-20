package com.ramit.models;

import jakarta.persistence.Id;
import lombok.Data;

@Data
public class Customer {
	@Id
	Integer customerId;
	String name;
	String email;
	String phoneNumber;
	Address address;
}
