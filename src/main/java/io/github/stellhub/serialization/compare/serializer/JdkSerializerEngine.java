package io.github.stellhub.serialization.compare.serializer;

import io.github.stellhub.serialization.compare.model.BenchmarkEnvelope;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class JdkSerializerEngine implements SerializerEngine {

	@Override
	public String name() {
		return "JDK";
	}

	@Override
	public byte[] serialize(BenchmarkEnvelope envelope) throws Exception {
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			 ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
			objectOutputStream.writeObject(envelope);
			objectOutputStream.flush();
			return outputStream.toByteArray();
		}
	}

	@Override
	public BenchmarkEnvelope deserialize(byte[] bytes) throws Exception {
		try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
			 ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
			return (BenchmarkEnvelope) objectInputStream.readObject();
		}
	}
}
