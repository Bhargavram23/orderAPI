package com.ramit.models;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "my-client", url = "http://localhost:7070")
public interface customerFeignClient {
	@PostMapping("/customer")
	public Customer upsertCustomerByExample(@RequestBody Customer customer);
}
