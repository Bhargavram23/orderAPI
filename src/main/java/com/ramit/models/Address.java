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
