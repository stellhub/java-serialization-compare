package io.github.stellhub.serialization.compare;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.stellhub.serialization.compare.benchmark.BenchmarkOptions;
import io.github.stellhub.serialization.compare.benchmark.BenchmarkReport;
import io.github.stellhub.serialization.compare.benchmark.BenchmarkRunner;
import org.junit.jupiter.api.Test;

class SerializationBenchmarkTest {

	/**
		 * 执行快速基准测试，确保报告生成链路可用。
		 */
	@Test
	void shouldGenerateBenchmarkReport() {
		BenchmarkRunner runner = new BenchmarkRunner();
		BenchmarkReport report = runner.run(BenchmarkOptions.quick());

		assertThat(report.metrics()).hasSize(72);
		assertThat(report.markdown()).contains("Java Serialization Compare Report");
		assertThat(report.markdown()).contains("JDK");
		assertThat(report.markdown()).contains("Protobuf");
		System.out.println(report.markdown());
	}
}
