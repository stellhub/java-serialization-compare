package io.github.stellhub.serialization.compare.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class KryoSerializerEngine implements SerializerEngine {

	private final ThreadLocal<Kryo> kryoHolder = ThreadLocal.withInitial(this::createKryo);

	@Override
	public String name() {
		return "Kryo";
	}

	@Override
	public byte[] serialize(BenchmarkEnvelope envelope) {
		Kryo kryo = kryoHolder.get();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try (Output output = new Output(outputStream)) {
			kryo.writeObject(output, envelope);
			output.flush();
			return outputStream.toByteArray();
		}
	}

	@Override
	public BenchmarkEnvelope deserialize(byte[] bytes) {
		Kryo kryo = kryoHolder.get();
		try (Input input = new Input(new ByteArrayInputStream(bytes))) {
			return kryo.readObject(input, BenchmarkEnvelope.class);
		}
	}

	private Kryo createKryo() {
		Kryo kryo = new Kryo();
		kryo.setRegistrationRequired(false);
		kryo.register(BenchmarkEnvelope.class);
		kryo.register(BinaryChunk.class);
		kryo.register(StructuredFlatPayload.class);
		kryo.register(StructuredNestedPayload.class);
		kryo.register(UnstructuredTextPayload.class);
		kryo.register(UnstructuredBinaryPayload.class);
		kryo.register(UserProfile.class);
		kryo.register(Address.class);
		kryo.register(Order.class);
		kryo.register(OrderItem.class);
		kryo.register(SessionInfo.class);
		kryo.register(ArrayList.class);
		kryo.register(LinkedHashMap.class);
		kryo.register(byte[].class);
		return kryo;
	}
}
