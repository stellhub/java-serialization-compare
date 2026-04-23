package io.github.stellhub.serialization.compare;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.stellhub.serialization.compare.model.BenchmarkEnvelope;
import io.github.stellhub.serialization.compare.scenario.BenchmarkDataFactory;
import io.github.stellhub.serialization.compare.scenario.BenchmarkScenario;
import io.github.stellhub.serialization.compare.scenario.BenchmarkScenarioFactory;
import io.github.stellhub.serialization.compare.serializer.SerializerEngine;
import io.github.stellhub.serialization.compare.serializer.SerializerRegistry;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SerializationRoundTripTest {

	/**
		 * 验证所有序列化实现都能对统一样本完成往返转换。
		 */
	@ParameterizedTest(name = "{0} -> {1}")
	@MethodSource("roundTripArguments")
	void shouldRoundTripForAllScenarios(String serializerName, BenchmarkScenario scenario, SerializerEngine serializer)
			throws Exception {
		BenchmarkEnvelope source = BenchmarkDataFactory.create(scenario);
		byte[] bytes = serializer.serialize(source);
		BenchmarkEnvelope target = serializer.deserialize(bytes);

		assertThat(bytes).isNotEmpty();
		assertThat(target).isEqualTo(source);
		assertThat(serializer.name()).isEqualTo(serializerName);
	}

	private static Stream<Arguments> roundTripArguments() {
		return SerializerRegistry.createDefaultSerializers().stream()
				.flatMap(serializer -> BenchmarkScenarioFactory.createAllScenarios().stream()
						.map(scenario -> Arguments.of(serializer.name(), scenario, serializer)));
	}
}
