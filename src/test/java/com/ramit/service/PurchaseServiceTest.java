package com.ramit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.Specification;

import com.ramit.Repositories.purchaseItemRepository;
import com.ramit.Repositories.purchaseRepository;
import com.ramit.dto.AdminOrderRequestDTO;
import com.ramit.dto.PurchaseSpecification;
import com.ramit.models.OrderTransferDTO;
import com.ramit.models.Purchase;
import com.ramit.models.PurchaseItem;

@SpringBootTest
@ExtendWith(value = { MockitoExtension.class })
public class PurchaseServiceTest {
	@MockBean
	purchaseRepository purchaseRepo;

	@MockBean
	purchaseItemRepository purchaseItemRepo;

	@InjectMocks
	PurchaseService purchaseServiceTest;

	@Test
	public void hadlePaymentTransactionTest() {

		Purchase purchase = new Purchase();
		when(purchaseRepo.save(any(Purchase.class))).thenReturn(purchase);
		Purchase result = purchaseServiceTest.handlePaymentTransaction(purchase);
		assertNotNull(result);
	}

	@Test
	public void verifyPaymentTest() {
		int order_id = 0;
		String razorpay_order_id = "RAZOR_PAY_TEST";
		String razorpay_id = "RAZOR_PAY_ID";
		Purchase purchase = new Purchase();
		when(purchaseRepo.findById(any(Integer.class))).thenReturn(Optional.of(purchase));
		when(purchaseRepo.save(any(Purchase.class))).thenReturn(purchase);
		boolean result = purchaseServiceTest.verifyPayment(order_id, razorpay_order_id, razorpay_id);
		assertTrue(result);
	}

	@Test
	public void FailPaymentTest() {
		int order_id = 0;
		Purchase purchase = new Purchase();
		when(purchaseRepo.findById(any(Integer.class))).thenReturn(Optional.of(purchase));
		when(purchaseRepo.save(any(Purchase.class))).thenReturn(purchase);
		boolean result = purchaseServiceTest.failPayment(order_id);
		assertTrue(result);
	}

	@Test
	public void getAdminDashboardOrderDetailsTest() {
		List<Purchase> purchaseList = new ArrayList<>();
		purchaseList.add(Purchase.builder().orderStatus("success").totalQuantity(3).totalPrice(10.0f).build());
		purchaseList.add(Purchase.builder().orderStatus("success").totalQuantity(2).totalPrice(20.0f).build());

		when(purchaseRepo.findByOrderStatus("success")).thenReturn(purchaseList);

		OrderTransferDTO adminDashboardOrderDetails = purchaseServiceTest.getAdminDashboardOrderDetails();

		assertEquals(5, adminDashboardOrderDetails.getTotalOrderQty());
		assertEquals(30.0f, adminDashboardOrderDetails.getTotalOrdersValue());
	}

	@Test
	public void getAllpurchaseItemsforOrderIdTest() {
		int order_id = 1;
		List<PurchaseItem> purchaseList = new ArrayList<>();
		purchaseList.add(PurchaseItem.builder().name("laptop").unitPrice(23f).quantity(2).build());
		purchaseList.add(PurchaseItem.builder().name("phone").unitPrice(255f).quantity(6).build());

		when(purchaseItemRepo.findByPurchase_OrderId(any(Integer.class))).thenReturn(purchaseList);
		assertNotNull(purchaseServiceTest.getAllpurchaseItemsforOrderId(order_id));

	}

	@Test
	public void getAllSuccessfulOrdersZeroLength() {
		AdminOrderRequestDTO admin_Order_request = new AdminOrderRequestDTO();
		List<Purchase> orders = null;
		Specification<Purchase> purchaseSpec = Specification
				.where(PurchaseSpecification.hasEmail(admin_Order_request.getEmail()))
				.and(PurchaseSpecification.hasStartDate(admin_Order_request.getStartDate()))
				.and(PurchaseSpecification.hasEndDate(admin_Order_request.getEndDate()));
		when(purchaseRepo.findAll(any(purchaseSpec.getClass()))).thenReturn(new ArrayList<Purchase>());

		List<Purchase> allSuccessfulOrders = purchaseServiceTest.getAllSuccessfulOrders(admin_Order_request);
		assertEquals(0, allSuccessfulOrders.size());

	}

	@Test
	public void getAllSuccessfulOrdersNonZeroLength() {
		AdminOrderRequestDTO admin_Order_request = new AdminOrderRequestDTO();
		List<Purchase> orders = new ArrayList<Purchase>();
		orders.add(Purchase.builder().email("rama@gmail.com").build());
		orders.add(Purchase.builder().email("sita@gmail.com").build());
		Specification<Purchase> purchaseSpec = Specification
				.where(PurchaseSpecification.hasEmail(admin_Order_request.getEmail()))
				.and(PurchaseSpecification.hasStartDate(admin_Order_request.getStartDate()))
				.and(PurchaseSpecification.hasEndDate(admin_Order_request.getEndDate()));
		when(purchaseRepo.findAll(any(purchaseSpec.getClass()))).thenReturn(orders);

		List<Purchase> allSuccessfulOrders = purchaseServiceTest.getAllSuccessfulOrders(admin_Order_request);
		assertEquals(2, allSuccessfulOrders.size());

	}

}
