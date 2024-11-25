package com.ramit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ramit.dto.CompleteOrderRequestDTO;
import com.ramit.models.Address;
import com.ramit.models.Customer;
import com.ramit.models.OrderTransferDTO;
import com.ramit.models.Purchase;
import com.ramit.models.PurchaseItem;
import com.ramit.models.customerFeignClient;
import com.ramit.service.PurchaseService;
import com.razorpay.Order;
import com.razorpay.OrderClient;
import com.razorpay.RazorpayClient;

@SpringBootTest
@ExtendWith(value = { MockitoExtension.class })
public class PaymentControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	paymentController paymentControllerTest;

	@MockBean
	PurchaseService purchaseService;

	@MockBean
	customerFeignClient fc;

	
	private RazorpayClient razorpayClient;
	
	private OrderClient orderClient;
	
	private Order mockOrder;
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(paymentControllerTest).build();
		objectMapper = new ObjectMapper(); // Initialize ObjectMapper
	}

	@Test
	public void getAdminDashboardOrderDetailsTest() throws Exception {
		OrderTransferDTO orderTransferDTOobj = OrderTransferDTO.builder().totalOrderQty(40).totalOrdersValue(500.0f)
				.totalSuccessfullOrders(52).build();
		when(purchaseService.getAdminDashboardOrderDetails()).thenReturn(orderTransferDTOobj);
		mockMvc.perform(get("/admin/ordersInfo").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.totalOrderQty").value(40))
				.andExpect(jsonPath("$.totalOrdersValue").value(500.0f))
				.andExpect(jsonPath("$.totalSuccessfullOrders").value(52));
	}

	@Test
	public void handlePaymentTransactionTest() throws Exception {

		PurchaseItem[] purchaseItems = new PurchaseItem[2];
		purchaseItems[0] = PurchaseItem.builder().name("DELL LAPTOP").unitPrice(19.9f).quantity(2).build();
		purchaseItems[1] = PurchaseItem.builder().name("APPLE IPHONE").unitPrice(60.0f).quantity(1).build();
		CompleteOrderRequestDTO completeRequest = CompleteOrderRequestDTO.builder().name("Sri Rama")
				.email("rama@gmail.com").phoneNumber("9381429451").line1("Ram Mandir").line2("Ayodhya").city("lucknow")
				.state("uttarpradesh").country("India").orderPrice(500f).orderQty(3).orderItems(purchaseItems).build();
		Address address = Address.builder().addressId(1).line1("Ram Mandir").line2("Ayodhya").city("lucknow")
				.state("uttarpradesh").country("India").build();
		Customer customer = Customer.builder().customerId(1).name("Sri Rama").email("rama@gmail.com")
				.phoneNumber("9381420451").address(address).build();

		List<PurchaseItem> purchaseItemsList = new ArrayList<PurchaseItem>(Arrays.asList(purchaseItems));

		Purchase purchase = Purchase.builder().orderId(1).totalPrice(500f).totalQuantity(3).email("rama@gmail.com")
				.customerId(1).shippingAddressId(1).purchaseItemsList(purchaseItemsList).build();

		when(fc.upsertCustomerByExample(any(Customer.class))).thenReturn(customer);
		when(purchaseService.handlePaymentTransaction(any(Purchase.class))).thenReturn(purchase);
// --------------------------------------------------------------------------------
      
       razorpayClient  = mock(RazorpayClient.class);
       OrderClient orderClient = mock(OrderClient.class);
      Order mockOrder = mock(Order.class);
       
       Field orderClientField = RazorpayClient.class.getDeclaredField("Orders");
       orderClientField.setAccessible(true); // Make private field accessible
       orderClientField.set(razorpayClient, orderClient);
       
       paymentControllerTest.razorpayClient = razorpayClient;
       
       when(razorpayClient.Orders.create(any())).thenReturn(mockOrder);
       when(mockOrder.get(any())).thenReturn("order_mock");

// ---------------------------------------------------------------------------------
       
		mockMvc.perform(post("/order").contentType("application/json")
				.content(objectMapper.writeValueAsString(completeRequest))) 
				.andExpect(status().isOk()) 
				.andExpect(jsonPath("$.name").value("Sri Rama"))
                .andExpect(jsonPath("$.email").value("rama@gmail.com"))
                .andExpect(jsonPath("$.phoneNumber").value("9381429451"))
                .andExpect(jsonPath("$.line1").value("Ram Mandir"))
                .andExpect(jsonPath("$.line2").value("Ayodhya"))
                .andExpect(jsonPath("$.city").value("lucknow"))
                .andExpect(jsonPath("$.state").value("uttarpradesh"))
                .andExpect(jsonPath("$.country").value("India"))
                .andExpect(jsonPath("$.orderId").value(1))
                .andExpect(jsonPath("$.razorPayOrderId").value("order_mock"))
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.orderPrice").value(500.0))
                .andExpect(jsonPath("$.orderQty").value(3))
                .andExpect(jsonPath("$.orderItems").isArray()) // Check if it's an array
                .andExpect(jsonPath("$.orderItems[0].name").value("DELL LAPTOP"))
                .andExpect(jsonPath("$.orderItems[0].quantity").value(2))
                .andExpect(jsonPath("$.orderItems[1].name").value("APPLE IPHONE"))
                .andExpect(jsonPath("$.orderItems[1].quantity").value(1));
	}

}
