package io.github.stellhub.serialization.compare.scenario;

import io.github.stellhub.serialization.compare.model.DatasetFormat;
import io.github.stellhub.serialization.compare.model.DatasetSize;
import java.util.ArrayList;
import java.util.List;

public final class BenchmarkScenarioFactory {

	private BenchmarkScenarioFactory() {
	}

	/**
		 * 生成完整的测试场景矩阵。
		 */
	public static List<BenchmarkScenario> createAllScenarios() {
		List<BenchmarkScenario> scenarios = new ArrayList<>();
		for (DatasetSize size : DatasetSize.values()) {
			for (DatasetFormat format : DatasetFormat.values()) {
				scenarios.add(BenchmarkScenario.builder()
						.code(size.name() + "_" + format.name())
						.size(size)
						.format(format)
						.nature(format.getNature())
						.seed(1_000L + size.ordinal() * 100L + format.ordinal())
						.sizeProfile(resolveProfile(size))
						.build());
			}
		}
		return List.copyOf(scenarios);
	}

	private static SizeProfile resolveProfile(DatasetSize size) {
		return switch (size) {
			case SMALL -> SizeProfile.builder()
					.tagCount(6)
					.attributeCount(8)
					.scoreCount(12)
					.descriptionLength(256)
					.orderCount(4)
					.itemsPerOrder(4)
					.sessionCount(6)
					.actionsPerSession(6)
					.interestCount(8)
					.summaryLength(320)
					.keywordCount(12)
					.rawTextLength(2_048)
					.rawJsonFieldCount(12)
					.binaryContentLength(4_096)
					.binaryChunkCount(6)
					.binaryChunkLength(256)
					.build();
			case MEDIUM -> SizeProfile.builder()
					.tagCount(24)
					.attributeCount(24)
					.scoreCount(64)
					.descriptionLength(2_048)
					.orderCount(18)
					.itemsPerOrder(8)
					.sessionCount(20)
					.actionsPerSession(10)
					.interestCount(20)
					.summaryLength(2_048)
					.keywordCount(24)
					.rawTextLength(24_576)
					.rawJsonFieldCount(48)
					.binaryContentLength(65_536)
					.binaryChunkCount(16)
					.binaryChunkLength(2_048)
					.build();
			case LARGE -> SizeProfile.builder()
					.tagCount(64)
					.attributeCount(80)
					.scoreCount(256)
					.descriptionLength(8_192)
					.orderCount(60)
					.itemsPerOrder(16)
					.sessionCount(64)
					.actionsPerSession(16)
					.interestCount(48)
					.summaryLength(8_192)
					.keywordCount(48)
					.rawTextLength(131_072)
					.rawJsonFieldCount(128)
					.binaryContentLength(262_144)
					.binaryChunkCount(32)
					.binaryChunkLength(8_192)
					.build();
		};
	}
}
