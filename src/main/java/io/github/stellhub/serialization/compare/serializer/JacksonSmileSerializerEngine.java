package io.github.stellhub.serialization.compare.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import io.github.stellhub.serialization.compare.model.BenchmarkEnvelope;

public class JacksonSmileSerializerEngine implements SerializerEngine {

	private final ObjectMapper objectMapper = new ObjectMapper(new SmileFactory()).findAndRegisterModules();

	@Override
	public String name() {
		return "Jackson-Smile";
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
