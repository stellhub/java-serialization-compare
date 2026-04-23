package io.github.stellhub.serialization.compare.benchmark;

import java.util.List;
import lombok.Builder;

@Builder
public record BenchmarkReport(BenchmarkOptions options, List<BenchmarkMetric> metrics, String markdown) {
}
