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
public class UnstructuredBinaryPayload implements Serializable {

	private String resourceId;
	private String mediaType;
	private byte[] content;
	private List<BinaryChunk> chunks;
	private Map<String, String> metadata;
	private String checksum;
}
