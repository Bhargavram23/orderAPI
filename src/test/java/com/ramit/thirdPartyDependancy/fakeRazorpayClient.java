package com.ramit.thirdPartyDependancy;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

public class fakeRazorpayClient extends RazorpayClient {
	public fakeOrderClient Orders;
	public fakeRazorpayClient(String key, String secret) throws RazorpayException {
		super(key, secret);	
		Orders = new fakeOrderClient();
	}
	

}
