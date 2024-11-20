package com.ramit.models;

import jakarta.persistence.Id;
import lombok.Data;

@Data
public class Address {
	@Id
	Integer addressId;
	String line1;
	String line2;
	String city;
	String state;
	String country;
	String zipcode;
	Integer customerId;
}
