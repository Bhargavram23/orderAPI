package com.ramit.controller;

import static org.mockito.Mockito.when;

import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.ramit.models.customerFeignClient;
import com.ramit.service.PurchaseService;

//@SpringBootTest
public class PaymentControllerTest {
	@InjectMocks
	paymentController paymentControllerObject;
	@MockBean
	PurchaseService purchaseService;
	@MockBean
	customerFeignClient fc;
	public void displayOrderDetailsToAdminTest(){
		when(purchaseService.getAdminDashboardOrderDetails()).then(null);
		
	}
	
}
