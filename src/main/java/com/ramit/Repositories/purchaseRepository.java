package com.ramit.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ramit.models.Purchase;
import com.ramit.models.PurchaseItem;

public interface purchaseRepository extends JpaRepository<Purchase, Integer>, JpaSpecificationExecutor<Purchase> {
	List<Purchase> findByOrderStatus(String status);
	PurchaseItem[] findPurchaseItemsByOrderId(int orderId);
}
