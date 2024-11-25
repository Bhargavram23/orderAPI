package com.ramit.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@Configuration
public class OrderConfiguration {
	
	final String RAZORPAY_KEY_ID = "rzp_test_UKY2DzhQPRLJoi";
	final String RAZORPAY_KEY_SECRET = "Z6PIQJVA8jl1PjGIxmN3QLnt";

    @Bean
    RazorpayClient getRazorpayClient() throws RazorpayException {
		return new RazorpayClient(RAZORPAY_KEY_ID, RAZORPAY_KEY_SECRET);
	}
	
}
