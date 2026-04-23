package io.github.stellhub.serialization.compare.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BenchmarkEnvelope implements Serializable {

	private String scenarioCode;
	private DatasetSize datasetSize;
	private DatasetFormat datasetFormat;
	private DataNature dataNature;
	private long seed;
	private StructuredFlatPayload structuredFlatPayload;
	private StructuredNestedPayload structuredNestedPayload;
	private UnstructuredTextPayload unstructuredTextPayload;
	private UnstructuredBinaryPayload unstructuredBinaryPayload;
}
