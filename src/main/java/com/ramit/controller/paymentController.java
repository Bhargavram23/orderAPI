package com.ramit.controller;

import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ramit.dto.AdminOrderRequestDTO;
import com.ramit.dto.CompleteOrderRequestDTO;
import com.ramit.models.Address;
import com.ramit.models.Customer;
import com.ramit.models.OrderTransferDTO;
import com.ramit.models.Purchase;
import com.ramit.models.PurchaseItem;
import com.ramit.models.TxnStatusResponse;
import com.ramit.models.customerFeignClient;
import com.ramit.service.PurchaseService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@CrossOrigin("http://localhost:4200")
@RestController
public class paymentController {
	@Autowired
	PurchaseService purchaseService;
	@Autowired
	customerFeignClient fc;

	final String RAZORPAY_KEY_ID = "rzp_test_UKY2DzhQPRLJoi";
	final String RAZORPAY_KEY_SECRET = "Z6PIQJVA8jl1PjGIxmN3QLnt";

	private final RazorpayClient razorpayClient;

	public paymentController() throws Exception {
		this.razorpayClient = new RazorpayClient(RAZORPAY_KEY_ID, RAZORPAY_KEY_SECRET);
	}
	
	@GetMapping("/admin/ordersInfo")
	public OrderTransferDTO getAdminDashboardOrderDetails() {
		return purchaseService.getAdminDashboardOrderDetails();
	}
	
	@PostMapping("/order")
	public CompleteOrderRequestDTO handlePaymentTransaction(@RequestBody CompleteOrderRequestDTO completeRequest)
			throws RazorpayException {

		// get the customer id and shipping id
		Customer customer = convertTocustomer(completeRequest, new Customer());
		Customer updateCustomer = fc.upsertCustomerByExample(customer);
		Purchase purchase = convertToPurchase(completeRequest, updateCustomer, new Purchase());

		// making the transaction
		Purchase savedPurchase = purchaseService.handlePaymentTransaction(purchase);

		// return successful transaction
		completeRequest.addressId = savedPurchase.getShippingAddressId();
		completeRequest.customerId = savedPurchase.getCustomerId();
		completeRequest.orderId = savedPurchase.getOrderId();

		// for razorpay order id
		JSONObject orderRequest = new JSONObject();
		int amount = (int) (completeRequest.getOrderPrice() * 100);
		orderRequest.put("amount", amount); // Amount in paise
		orderRequest.put("currency", "INR");
		orderRequest.put("receipt", "receipt#" + completeRequest.orderId);

		completeRequest.razorPayOrderId = razorpayClient.Orders.create(orderRequest).get("id");


		return completeRequest;
	}

	@PostMapping("/verify")
	public TxnStatusResponse verifyPaymentTransaction(@RequestBody CompleteOrderRequestDTO completeRequest) {

		if (completeRequest.getRazorPayId() != null) {

			return purchaseService.verifyPayment(completeRequest.orderId, completeRequest.getRazorPayOrderId(),
					completeRequest.getRazorPayId()) ? new TxnStatusResponse("success")
							: new TxnStatusResponse("NA");
		} else {
			return purchaseService.failPayment(completeRequest.orderId) ? new TxnStatusResponse("fail")
					: new TxnStatusResponse("NA");
		}
	}
	
	@PostMapping("/admin/orders")
	public List<Purchase> ViewAllSuccessfulOrders(@RequestBody AdminOrderRequestDTO admin_Order_request) {
		return purchaseService.getAllSuccessfulOrders(admin_Order_request);
	}
	
	@GetMapping("/admin/orders/{order_id}")
	public List<PurchaseItem> ViewAllPurchaseItems(@PathVariable int order_id){
		return purchaseService.getAllpurchaseItemsforOrderId(order_id);
	}
	
	private Customer convertTocustomer(CompleteOrderRequestDTO completeRequest, Customer customer) {

		// convert a request body object to customer for api communication
		customer.setName(completeRequest.name);
		customer.setEmail(completeRequest.email);
		customer.setPhoneNumber(completeRequest.phoneNumber);
		Address address = new Address();
		address.setLine1(completeRequest.line1);
		address.setLine2(completeRequest.line2);
		address.setCity(completeRequest.getCity());
		address.setCountry(completeRequest.getCountry());
		address.setState(completeRequest.getState());
		address.setCountry(completeRequest.getCountry());
		address.setZipcode(completeRequest.getZipcode());
		customer.setAddress(address);
		return customer;
	}

	private Purchase convertToPurchase(CompleteOrderRequestDTO completeRequest, Customer upserCustomerByExample,
			Purchase purchase) {
		purchase.setCustomerId(upserCustomerByExample.getCustomerId());
		purchase.setShippingAddressId(upserCustomerByExample.getAddress().getAddressId());
		purchase.setTotalPrice(completeRequest.getOrderPrice());
		purchase.setTotalQuantity(completeRequest.getOrderQty());

		for (PurchaseItem item : completeRequest.orderItems) {
			item.setPurchase(purchase);
		}

		purchase.setPurchaseItemsList(Arrays.asList(completeRequest.orderItems));

		return purchase;
	}

}
