package com.ramit.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderTransferDTO {
int totalSuccessfullOrders;
float totalOrdersValue;
int totalOrderQty;
}
