package io.github.stellhub.serialization.compare.scenario;

import lombok.Builder;

@Builder
public record SizeProfile(
		int tagCount,
		int attributeCount,
		int scoreCount,
		int descriptionLength,
		int orderCount,
		int itemsPerOrder,
		int sessionCount,
		int actionsPerSession,
		int interestCount,
		int summaryLength,
		int keywordCount,
		int rawTextLength,
		int rawJsonFieldCount,
		int binaryContentLength,
		int binaryChunkCount,
		int binaryChunkLength) {
}
