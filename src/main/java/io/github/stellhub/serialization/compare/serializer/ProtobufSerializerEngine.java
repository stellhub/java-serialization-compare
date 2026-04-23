package io.github.stellhub.serialization.compare.serializer;

import io.github.stellhub.serialization.compare.model.BenchmarkEnvelope;
import io.github.stellhub.serialization.compare.protobuf.BenchmarkEnvelopeProto;
import io.github.stellhub.serialization.compare.protobuf.ProtobufMapper;

public class ProtobufSerializerEngine implements SerializerEngine {

	private final ProtobufMapper mapper = new ProtobufMapper();

	@Override
	public String name() {
		return "Protobuf";
	}

	@Override
	public byte[] serialize(BenchmarkEnvelope envelope) {
		return mapper.toProto(envelope).toByteArray();
	}

	@Override
	public BenchmarkEnvelope deserialize(byte[] bytes) throws Exception {
		return mapper.fromProto(BenchmarkEnvelopeProto.parseFrom(bytes));
	}
}
