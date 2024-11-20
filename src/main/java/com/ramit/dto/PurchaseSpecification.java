package com.ramit.dto;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.ramit.models.Purchase;

public class PurchaseSpecification {
	 public static Specification<Purchase> hasEmail(String email) {
	        return (root, query, criteriaBuilder) -> {
	            if (email == null || email.isEmpty()) {
	                return criteriaBuilder.conjunction();
	            }
	            return criteriaBuilder.equal(root.get("email"), email);
	        };
	    }
	 public static Specification<Purchase> hasStartDate(LocalDateTime startDate) {
	        return (root, query, criteriaBuilder) -> {
	            if (startDate == null) {
	                return criteriaBuilder.conjunction();
	            }
	            return criteriaBuilder.greaterThanOrEqualTo(root.get("dateCreated"), startDate);
	        };
	    }

	 public static Specification<Purchase> hasEndDate(LocalDateTime endDate) {
	        return (root, query, criteriaBuilder) -> {
	            if (endDate == null) {
	                return criteriaBuilder.conjunction();
	            }
	            return criteriaBuilder.lessThanOrEqualTo(root.get("dateCreated"), endDate);
	        };
	    }
}
