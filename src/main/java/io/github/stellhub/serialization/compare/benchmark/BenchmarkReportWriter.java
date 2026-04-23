package io.github.stellhub.serialization.compare.benchmark;

import io.github.stellhub.serialization.compare.model.DatasetFormat;
import io.github.stellhub.serialization.compare.model.DatasetSize;
import io.github.stellhub.serialization.compare.scenario.BenchmarkScenario;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public final class BenchmarkReportWriter {

	private BenchmarkReportWriter() {
	}

	/**
		 * 生成 Markdown 报告内容。
		 */
	public static String toMarkdown(BenchmarkReport report) {
		StringBuilder builder = new StringBuilder(8_192);
		builder.append("# Java Serialization Compare Report").append(System.lineSeparator()).append(System.lineSeparator());
		builder.append("对比维度：相同业务语义数据，在大小、格式、结构化/非结构化条件下，测试不同序列化方案的体积与性能。")
				.append(System.lineSeparator()).append(System.lineSeparator());
		builder.append("| 场景 | 序列化方式 | 字节数 | 序列化均值(us/op) | 反序列化均值(us/op) | 序列化吞吐量(op/s) | 反序列化吞吐量(op/s) |")
				.append(System.lineSeparator());
		builder.append("| --- | --- | ---: | ---: | ---: | ---: | ---: |").append(System.lineSeparator());

		List<BenchmarkMetric> orderedMetrics = report.metrics().stream()
				.sorted(Comparator
						.comparing((BenchmarkMetric metric) -> metric.scenario().size().ordinal())
						.thenComparing(metric -> metric.scenario().format().ordinal())
						.thenComparing(BenchmarkMetric::serializerName))
				.toList();

		for (BenchmarkMetric metric : orderedMetrics) {
			builder.append("| ")
					.append(metric.scenario().displayName())
					.append(" | ")
					.append(metric.serializerName())
					.append(" | ")
					.append(metric.serializedBytes())
					.append(" | ")
					.append(formatDecimal(metric.serializeAvgMicros()))
					.append(" | ")
					.append(formatDecimal(metric.deserializeAvgMicros()))
					.append(" | ")
					.append(formatDecimal(metric.serializeThroughput()))
					.append(" | ")
					.append(formatDecimal(metric.deserializeThroughput()))
					.append(" |")
					.append(System.lineSeparator());
		}

		builder.append(System.lineSeparator());
		builder.append("## 场景摘要").append(System.lineSeparator()).append(System.lineSeparator());
		Map<DatasetSize, Map<DatasetFormat, List<BenchmarkMetric>>> grouped = report.metrics().stream()
				.collect(Collectors.groupingBy(metric -> metric.scenario().size(),
						Collectors.groupingBy(metric -> metric.scenario().format())));

		for (DatasetSize size : DatasetSize.values()) {
			builder.append("### ").append(size.getDisplayName()).append("数据").append(System.lineSeparator());
			Map<DatasetFormat, List<BenchmarkMetric>> byFormat = grouped.get(size);
			for (DatasetFormat format : DatasetFormat.values()) {
				if (byFormat == null || !byFormat.containsKey(format)) {
					continue;
				}
				List<BenchmarkMetric> metrics = byFormat.get(format);
				BenchmarkMetric smallest = metrics.stream().min(Comparator.comparingInt(BenchmarkMetric::serializedBytes)).orElseThrow();
				BenchmarkMetric fastestSerialize = metrics.stream().min(Comparator.comparingDouble(BenchmarkMetric::serializeAvgMicros)).orElseThrow();
				BenchmarkMetric fastestDeserialize = metrics.stream().min(Comparator.comparingDouble(BenchmarkMetric::deserializeAvgMicros)).orElseThrow();
				builder.append("- ")
						.append(format.getDisplayName())
						.append("：最小体积=")
						.append(smallest.serializerName())
						.append("，最快序列化=")
						.append(fastestSerialize.serializerName())
						.append("，最快反序列化=")
						.append(fastestDeserialize.serializerName())
						.append(System.lineSeparator());
			}
			builder.append(System.lineSeparator());
		}
		return builder.toString();
	}

	/**
		 * 将报告写入 target 目录。
		 */
	public static void writeToTarget(BenchmarkReport report) throws IOException {
		Path targetDirectory = Path.of("target");
		Files.createDirectories(targetDirectory);
		Files.writeString(targetDirectory.resolve("serialization-benchmark-report.md"), report.markdown());
	}

	private static String formatDecimal(double value) {
		return String.format(Locale.ROOT, "%.2f", value);
	}
}
