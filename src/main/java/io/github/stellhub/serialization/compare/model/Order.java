package io.github.stellhub.serialization.compare.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {

	private String orderId;
	private long createdAtEpochMillis;
	private List<OrderItem> items;
	private double totalAmount;
	private Map<String, String> attributes;
}
