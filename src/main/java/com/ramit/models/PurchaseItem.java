package com.ramit.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer purchaseItemId;
	@Column(name = "product_id")
	Integer id;
	String name;
	String imageUrl;
	Float unitPrice;
	Integer quantity;
	@ManyToOne()
	@JoinColumn(name = "fk")
	@JsonIgnore
	Purchase purchase;
	
}
