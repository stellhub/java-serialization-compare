package io.github.stellhub.serialization.compare.benchmark;

import io.github.stellhub.serialization.compare.model.BenchmarkEnvelope;
import io.github.stellhub.serialization.compare.scenario.BenchmarkDataFactory;
import io.github.stellhub.serialization.compare.scenario.BenchmarkScenario;
import io.github.stellhub.serialization.compare.scenario.BenchmarkScenarioFactory;
import io.github.stellhub.serialization.compare.serializer.SerializerEngine;
import io.github.stellhub.serialization.compare.serializer.SerializerRegistry;
import java.util.ArrayList;
import java.util.List;

public class BenchmarkRunner {

	private final List<SerializerEngine> serializers;
	private final List<BenchmarkScenario> scenarios;

	public BenchmarkRunner() {
		this(SerializerRegistry.createDefaultSerializers(), BenchmarkScenarioFactory.createAllScenarios());
	}

	public BenchmarkRunner(List<SerializerEngine> serializers, List<BenchmarkScenario> scenarios) {
		this.serializers = serializers;
		this.scenarios = scenarios;
	}

	/**
		 * 执行完整的序列化对比测试。
		 */
	public BenchmarkReport run(BenchmarkOptions options) {
		List<BenchmarkMetric> metrics = new ArrayList<>();
		for (BenchmarkScenario scenario : scenarios) {
			BenchmarkEnvelope envelope = BenchmarkDataFactory.create(scenario);
			for (SerializerEngine serializer : serializers) {
				metrics.add(runSingle(serializer, scenario, envelope, options));
			}
		}

		BenchmarkReport baseReport = BenchmarkReport.builder()
				.options(options)
				.metrics(List.copyOf(metrics))
				.build();
		String markdown = BenchmarkReportWriter.toMarkdown(baseReport);
		BenchmarkReport report = BenchmarkReport.builder()
				.options(options)
				.metrics(baseReport.metrics())
				.markdown(markdown)
				.build();
		if (options.writeReportToTarget()) {
			try {
				BenchmarkReportWriter.writeToTarget(report);
			} catch (Exception exception) {
				throw new IllegalStateException("Failed to write benchmark report", exception);
			}
		}
		return report;
	}

	private BenchmarkMetric runSingle(
			SerializerEngine serializer,
			BenchmarkScenario scenario,
			BenchmarkEnvelope envelope,
			BenchmarkOptions options) {
		try {
			for (int index = 0; index < options.warmupIterations(); index++) {
				byte[] warmupBytes = serializer.serialize(envelope);
				serializer.deserialize(warmupBytes);
			}

			long serializeStart = System.nanoTime();
			byte[] serializedBytes = null;
			for (int index = 0; index < options.measureIterations(); index++) {
				serializedBytes = serializer.serialize(envelope);
			}
			long serializeCost = System.nanoTime() - serializeStart;
			if (serializedBytes == null) {
				throw new IllegalStateException("Serialized bytes must not be null");
			}

			validateRoundTrip(serializer, envelope, serializedBytes);

			long deserializeStart = System.nanoTime();
			for (int index = 0; index < options.measureIterations(); index++) {
				serializer.deserialize(serializedBytes);
			}
			long deserializeCost = System.nanoTime() - deserializeStart;

			return BenchmarkMetric.builder()
					.scenario(scenario)
					.serializerName(serializer.name())
					.serializedBytes(serializedBytes.length)
					.serializeAvgMicros(toAverageMicros(serializeCost, options.measureIterations()))
					.deserializeAvgMicros(toAverageMicros(deserializeCost, options.measureIterations()))
					.serializeThroughput(toThroughput(serializeCost, options.measureIterations()))
					.deserializeThroughput(toThroughput(deserializeCost, options.measureIterations()))
					.build();
		} catch (Exception exception) {
			throw new IllegalStateException(
					"Benchmark failed for serializer=%s, scenario=%s".formatted(serializer.name(), scenario.code()),
					exception);
		}
	}

	private void validateRoundTrip(SerializerEngine serializer, BenchmarkEnvelope expected, byte[] bytes) throws Exception {
		BenchmarkEnvelope actual = serializer.deserialize(bytes);
		if (!expected.equals(actual)) {
			throw new IllegalStateException("Round trip validation failed for serializer " + serializer.name());
		}
	}

	private double toAverageMicros(long totalNanos, int iterations) {
		return totalNanos / 1_000D / iterations;
	}

	private double toThroughput(long totalNanos, int iterations) {
		return (1_000_000_000D * iterations) / totalNanos;
	}
}
