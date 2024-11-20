package com.ramit.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ramit.models.PurchaseItem;

public interface purchaseItemRepository extends JpaRepository<PurchaseItem, Integer> {
	
	List<PurchaseItem> findByPurchase_OrderId(int orderId);

}
