package io.github.stellhub.serialization.compare.scenario;

import io.github.stellhub.serialization.compare.model.Address;
import io.github.stellhub.serialization.compare.model.BenchmarkEnvelope;
import io.github.stellhub.serialization.compare.model.BinaryChunk;
import io.github.stellhub.serialization.compare.model.Order;
import io.github.stellhub.serialization.compare.model.OrderItem;
import io.github.stellhub.serialization.compare.model.SessionInfo;
import io.github.stellhub.serialization.compare.model.StructuredFlatPayload;
import io.github.stellhub.serialization.compare.model.StructuredNestedPayload;
import io.github.stellhub.serialization.compare.model.UnstructuredBinaryPayload;
import io.github.stellhub.serialization.compare.model.UnstructuredTextPayload;
import io.github.stellhub.serialization.compare.model.UserProfile;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public final class BenchmarkDataFactory {

	private static final String[] DEVICES = {"ios", "android", "mac", "windows", "linux"};
	private static final String[] ACTIONS = {
		"open-home",
		"search",
		"click-banner",
		"add-cart",
		"submit-order",
		"pay-success",
		"logout"
	};

	private BenchmarkDataFactory() {
	}

	/**
		 * 根据场景生成统一数据样本。
		 */
	public static BenchmarkEnvelope create(BenchmarkScenario scenario) {
		Random random = new Random(scenario.seed());
		BenchmarkEnvelope.BenchmarkEnvelopeBuilder builder = BenchmarkEnvelope.builder()
				.scenarioCode(scenario.code())
				.datasetSize(scenario.size())
				.datasetFormat(scenario.format())
				.dataNature(scenario.nature())
				.seed(scenario.seed());

		switch (scenario.format()) {
			case STRUCTURED_FLAT -> builder.structuredFlatPayload(createStructuredFlatPayload(scenario, random));
			case STRUCTURED_NESTED -> builder.structuredNestedPayload(createStructuredNestedPayload(scenario, random));
			case UNSTRUCTURED_TEXT -> builder.unstructuredTextPayload(createUnstructuredTextPayload(scenario, random));
			case UNSTRUCTURED_BINARY -> builder.unstructuredBinaryPayload(createUnstructuredBinaryPayload(scenario, random));
		}
		return builder.build();
	}

	private static StructuredFlatPayload createStructuredFlatPayload(BenchmarkScenario scenario, Random random) {
		SizeProfile profile = scenario.sizeProfile();
		return StructuredFlatPayload.builder()
				.recordId("record-" + scenario.code())
				.tenantId("tenant-" + scenario.size().name().toLowerCase())
				.sequence(Math.abs(random.nextLong()))
				.amount(random.nextDouble(10_000D, 99_999D))
				.active(true)
				.tags(generateStrings("tag", profile.tagCount(), random, 8))
				.attributes(generateMap("attr", profile.attributeCount(), random, 16))
				.scores(generateScores(profile.scoreCount(), random))
				.description(generateParagraph("flat-" + scenario.code(), profile.descriptionLength(), random))
				.build();
	}

	private static StructuredNestedPayload createStructuredNestedPayload(BenchmarkScenario scenario, Random random) {
		SizeProfile profile = scenario.sizeProfile();
		UserProfile user = UserProfile.builder()
				.userId("user-" + scenario.code())
				.account("account_" + random.nextInt(10_000))
				.displayName("display-" + scenario.size().name().toLowerCase())
				.age(random.nextInt(20, 55))
				.interests(generateStrings("interest", profile.interestCount(), random, 10))
				.primaryAddress(Address.builder()
						.country("CN")
						.province("Shanghai")
						.city("Shanghai")
						.district("Pudong")
						.street(generateParagraph("street", 32, random))
						.postalCode(String.valueOf(200000 + random.nextInt(10_000)))
						.build())
				.build();

		List<Order> orders = new ArrayList<>(profile.orderCount());
		for (int orderIndex = 0; orderIndex < profile.orderCount(); orderIndex++) {
			List<OrderItem> items = new ArrayList<>(profile.itemsPerOrder());
			double total = 0D;
			for (int itemIndex = 0; itemIndex < profile.itemsPerOrder(); itemIndex++) {
				int quantity = random.nextInt(1, 6);
				double price = random.nextDouble(10D, 300D);
				total += quantity * price;
				items.add(OrderItem.builder()
						.sku("sku-" + orderIndex + "-" + itemIndex)
						.name(generateParagraph("product", 18, random))
						.quantity(quantity)
						.price(price)
						.build());
			}
			orders.add(Order.builder()
					.orderId("order-" + scenario.code() + "-" + orderIndex)
					.createdAtEpochMillis(1_713_960_000_000L + orderIndex * 60_000L)
					.items(items)
					.totalAmount(total)
					.attributes(generateMap("order-attr", Math.max(4, profile.attributeCount() / 4), random, 12))
					.build());
		}

		List<SessionInfo> sessions = new ArrayList<>(profile.sessionCount());
		for (int sessionIndex = 0; sessionIndex < profile.sessionCount(); sessionIndex++) {
			List<String> actions = new ArrayList<>(profile.actionsPerSession());
			for (int actionIndex = 0; actionIndex < profile.actionsPerSession(); actionIndex++) {
				actions.add(ACTIONS[(sessionIndex + actionIndex) % ACTIONS.length] + "-" + actionIndex);
			}
			sessions.add(SessionInfo.builder()
					.sessionId("session-" + scenario.code() + "-" + sessionIndex)
					.device(DEVICES[sessionIndex % DEVICES.length])
					.ip("10.0." + (sessionIndex % 255) + "." + ((sessionIndex * 13) % 255))
					.recentActions(actions)
					.build());
		}

		return StructuredNestedPayload.builder()
				.user(user)
				.orders(orders)
				.sessions(sessions)
				.labels(generateMap("label", profile.attributeCount(), random, 14))
				.summary(generateParagraph("summary-" + scenario.code(), profile.summaryLength(), random))
				.build();
	}

	private static UnstructuredTextPayload createUnstructuredTextPayload(BenchmarkScenario scenario, Random random) {
		SizeProfile profile = scenario.sizeProfile();
		return UnstructuredTextPayload.builder()
				.documentId("doc-" + scenario.code())
				.title("document-title-" + scenario.size().name().toLowerCase())
				.language("zh-CN")
				.keywords(generateStrings("keyword", profile.keywordCount(), random, 12))
				.rawText(generateParagraph("article-" + scenario.code(), profile.rawTextLength(), random))
				.rawJsonFragment(generateJsonFragment(profile.rawJsonFieldCount(), random))
				.metadata(generateMap("meta", profile.attributeCount(), random, 18))
				.build();
	}

	private static UnstructuredBinaryPayload createUnstructuredBinaryPayload(BenchmarkScenario scenario, Random random) {
		SizeProfile profile = scenario.sizeProfile();
		byte[] content = generateBytes(profile.binaryContentLength(), random);
		List<BinaryChunk> chunks = new ArrayList<>(profile.binaryChunkCount());
		for (int index = 0; index < profile.binaryChunkCount(); index++) {
			chunks.add(BinaryChunk.builder()
					.data(generateBytes(profile.binaryChunkLength(), random))
					.build());
		}
		return UnstructuredBinaryPayload.builder()
				.resourceId("resource-" + scenario.code())
				.mediaType("application/octet-stream")
				.content(content)
				.chunks(chunks)
				.metadata(generateMap("bin-meta", profile.attributeCount(), random, 10))
				.checksum(calculateSha256(content))
				.build();
	}

	private static List<String> generateStrings(String prefix, int count, Random random, int wordLength) {
		List<String> values = new ArrayList<>(count);
		for (int index = 0; index < count; index++) {
			values.add(prefix + "-" + index + "-" + randomText(wordLength, random));
		}
		return values;
	}

	private static Map<String, String> generateMap(String prefix, int count, Random random, int valueLength) {
		Map<String, String> values = new LinkedHashMap<>(count);
		for (int index = 0; index < count; index++) {
			values.put(prefix + "-" + index, randomText(valueLength, random));
		}
		return values;
	}

	private static List<Long> generateScores(int count, Random random) {
		List<Long> values = new ArrayList<>(count);
		for (int index = 0; index < count; index++) {
			values.add(random.nextLong(0L, 100_000L));
		}
		return values;
	}

	private static String generateParagraph(String prefix, int targetLength, Random random) {
		StringBuilder builder = new StringBuilder(targetLength + prefix.length() + 32);
		builder.append(prefix).append(' ');
		while (builder.length() < targetLength) {
			builder.append(randomText(12, random)).append(' ');
		}
		return builder.substring(0, targetLength);
	}

	private static String generateJsonFragment(int fieldCount, Random random) {
		StringBuilder builder = new StringBuilder(fieldCount * 32);
		builder.append('{');
		for (int index = 0; index < fieldCount; index++) {
			if (index > 0) {
				builder.append(',');
			}
			builder.append("\"field").append(index).append("\":\"")
					.append(randomText(18, random))
					.append('"');
		}
		builder.append('}');
		return builder.toString();
	}

	private static byte[] generateBytes(int length, Random random) {
		byte[] bytes = new byte[length];
		random.nextBytes(bytes);
		return bytes;
	}

	private static String randomText(int length, Random random) {
		byte[] bytes = new byte[length];
		for (int index = 0; index < length; index++) {
			bytes[index] = (byte) ('a' + random.nextInt(26));
		}
		return new String(bytes, StandardCharsets.UTF_8);
	}

	private static String calculateSha256(byte[] bytes) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			return HexFormat.of().formatHex(digest.digest(bytes));
		} catch (NoSuchAlgorithmException exception) {
			throw new IllegalStateException("SHA-256 algorithm unavailable", exception);
		}
	}
}
