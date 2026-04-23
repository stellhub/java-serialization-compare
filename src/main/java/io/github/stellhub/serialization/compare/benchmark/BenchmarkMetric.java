package io.github.stellhub.serialization.compare.benchmark;

import io.github.stellhub.serialization.compare.scenario.BenchmarkScenario;
import lombok.Builder;

@Builder
public record BenchmarkMetric(
		BenchmarkScenario scenario,
		String serializerName,
		int serializedBytes,
		double serializeAvgMicros,
		double deserializeAvgMicros,
		double serializeThroughput,
		double deserializeThroughput) {
}
