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
public class UnstructuredTextPayload implements Serializable {

	private String documentId;
	private String title;
	private String language;
	private List<String> keywords;
	private String rawText;
	private String rawJsonFragment;
	private Map<String, String> metadata;
}
