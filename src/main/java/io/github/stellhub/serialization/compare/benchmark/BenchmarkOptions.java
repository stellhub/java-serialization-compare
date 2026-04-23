package io.github.stellhub.serialization.compare.benchmark;

import lombok.Builder;

@Builder
public record BenchmarkOptions(int warmupIterations, int measureIterations, boolean writeReportToTarget) {

	/**
		 * 生成默认的完整对比配置。
		 */
	public static BenchmarkOptions standard() {
		return BenchmarkOptions.builder()
				.warmupIterations(40)
				.measureIterations(120)
				.writeReportToTarget(true)
				.build();
	}

	/**
		 * 生成适合测试阶段的快速配置。
		 */
	public static BenchmarkOptions quick() {
		return BenchmarkOptions.builder()
				.warmupIterations(5)
				.measureIterations(12)
				.writeReportToTarget(false)
				.build();
	}
}
