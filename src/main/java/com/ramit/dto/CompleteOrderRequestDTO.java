package com.ramit.dto;

import com.ramit.models.PurchaseItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompleteOrderRequestDTO {
		public PurchaseItem[] orderItems;
		public Integer orderId;
		public Integer orderQty;
		public Float orderPrice;
		public String razorPayId;
		public Integer customerId;
		public String name;
		public String email;
		public String phoneNumber;
		public Integer addressId;
		public String line1;
		public String line2;
		public String city;
		public String state;
		public String country;
		public String zipcode;
		public String razorPayOrderId;

	}
