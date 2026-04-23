package io.github.stellhub.serialization.compare;

import io.github.stellhub.serialization.compare.benchmark.BenchmarkOptions;
import io.github.stellhub.serialization.compare.benchmark.BenchmarkReport;
import io.github.stellhub.serialization.compare.benchmark.BenchmarkRunner;

public final class SerializationBenchmarkApplication {

	private SerializationBenchmarkApplication() {
	}

	/**
		 * 运行完整的序列化对比测试并输出报告。
		 */
	public static void main(String[] args) {
		BenchmarkRunner runner = new BenchmarkRunner();
		BenchmarkReport report = runner.run(BenchmarkOptions.standard());
		System.out.println(report.markdown());
	}
}
