package io.github.stellhub.serialization.compare.scenario;

import io.github.stellhub.serialization.compare.model.DataNature;
import io.github.stellhub.serialization.compare.model.DatasetFormat;
import io.github.stellhub.serialization.compare.model.DatasetSize;
import lombok.Builder;

@Builder
public record BenchmarkScenario(
		String code,
		DatasetSize size,
		DatasetFormat format,
		DataNature nature,
		long seed,
		SizeProfile sizeProfile) {

	/**
		 * 返回用于报表展示的场景名称。
		 */
	public String displayName() {
		return size.getDisplayName() + " / " + format.getDisplayName();
	}
}
