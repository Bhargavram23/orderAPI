package com.ramit.models;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Purchase Table")
public class Purchase {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer orderId;
	Integer totalQuantity;
	Float totalPrice;
	String orderStatus;
	String email;
	@CreationTimestamp
	LocalDateTime dateCreated;
	@UpdateTimestamp
	LocalDateTime lastCreated;
	Integer customerId;
	String razorPayId;
	String razorPayOrderId;
	Integer shippingAddressId;
	@OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL)
	List<PurchaseItem> purchaseItemsList;
}
