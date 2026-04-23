package io.github.stellhub.serialization.compare.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.stellhub.serialization.compare.model.BenchmarkEnvelope;

public class JacksonJsonSerializerEngine implements SerializerEngine {

	private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

	@Override
	public String name() {
		return "Jackson-JSON";
	}

	@Override
	public byte[] serialize(BenchmarkEnvelope envelope) throws Exception {
		return objectMapper.writeValueAsBytes(envelope);
	}

	@Override
	public BenchmarkEnvelope deserialize(byte[] bytes) throws Exception {
		return objectMapper.readValue(bytes, BenchmarkEnvelope.class);
	}
}
