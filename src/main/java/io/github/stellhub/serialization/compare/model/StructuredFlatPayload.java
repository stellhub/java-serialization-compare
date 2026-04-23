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
public class StructuredFlatPayload implements Serializable {

	private String recordId;
	private String tenantId;
	private long sequence;
	private double amount;
	private boolean active;
	private List<String> tags;
	private Map<String, String> attributes;
	private List<Long> scores;
	private String description;
}
