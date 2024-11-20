package com.ramit.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ramit.Repositories.purchaseItemRepository;
import com.ramit.Repositories.purchaseRepository;
import com.ramit.dto.AdminOrderRequestDTO;
import com.ramit.dto.PurchaseSpecification;
import com.ramit.models.Customer;
import com.ramit.models.OrderTransferDTO;
import com.ramit.models.Purchase;
import com.ramit.models.PurchaseItem;

@Service
public class PurchaseService {

	@Autowired
	purchaseRepository purchaseRepo;
	
	@Autowired
	purchaseItemRepository purchaseItemRepo;

	public Purchase handlePaymentTransaction(Customer customer, Purchase purchase) {
		return purchaseRepo.save(purchase);
	}

	public boolean verifyPayment(int order_id, String razorpay_order_id, String razorpay_id) {
		Purchase purchaseInDB = purchaseRepo.findById(order_id).get();
		purchaseInDB.setRazorPayId(razorpay_id);
		purchaseInDB.setRazorPayOrderId(razorpay_order_id);
		purchaseInDB.setOrderStatus("success");
		purchaseRepo.save(purchaseInDB);
		return true;
	}

	public boolean failPayment(int order_id) {
		Purchase purchaseInDB = purchaseRepo.findById(order_id).get();
		purchaseInDB.setOrderStatus("failed");
		purchaseRepo.save(purchaseInDB);
		return true;
	}

	public OrderTransferDTO getAdminDashboardOrderDetails() {
		OrderTransferDTO oTD = new OrderTransferDTO();
		for (Purchase purchase : purchaseRepo.findByOrderStatus("success")) {
			oTD.setTotalOrderQty(oTD.getTotalOrderQty() + purchase.getTotalQuantity());
			oTD.setTotalOrdersValue(oTD.getTotalOrdersValue() + purchase.getTotalPrice());
			oTD.setTotalSuccessfullOrders(oTD.getTotalSuccessfullOrders() + 1);
		}
		return oTD;
	}

	public List<Purchase> getAllSuccessfulOrders(AdminOrderRequestDTO admin_Order_request) {
		Specification<Purchase> purchaseSpec = Specification
				.where(PurchaseSpecification.hasEmail(admin_Order_request.getEmail()))
				.and(PurchaseSpecification.hasStartDate(admin_Order_request.getStartDate()))
				.and(PurchaseSpecification.hasEndDate(admin_Order_request.getEndDate()));
		List<Purchase> orders = purchaseRepo.findAll(purchaseSpec);
		if (orders.size() == 0) {
			return Collections.emptyList();
		}
		return orders;
	}


	public List<PurchaseItem> getAllpurchaseItemsforOrderId(int order_id) {
		return purchaseItemRepo.findByPurchase_OrderId(order_id);
		
	}

}
