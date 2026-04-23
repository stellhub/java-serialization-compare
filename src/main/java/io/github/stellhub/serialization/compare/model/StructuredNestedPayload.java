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
public class StructuredNestedPayload implements Serializable {

	private UserProfile user;
	private List<Order> orders;
	private List<SessionInfo> sessions;
	private Map<String, String> labels;
	private String summary;
}
