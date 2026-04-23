package io.github.stellhub.serialization.compare.serializer;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import io.github.stellhub.serialization.compare.model.BenchmarkEnvelope;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class Hessian2SerializerEngine implements SerializerEngine {

	@Override
	public String name() {
		return "Hessian2";
	}

	@Override
	public byte[] serialize(BenchmarkEnvelope envelope) throws Exception {
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			Hessian2Output hessian2Output = new Hessian2Output(outputStream);
			hessian2Output.writeObject(envelope);
			hessian2Output.flush();
			return outputStream.toByteArray();
		}
	}

	@Override
	public BenchmarkEnvelope deserialize(byte[] bytes) throws Exception {
		try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes)) {
			Hessian2Input hessian2Input = new Hessian2Input(inputStream);
			return (BenchmarkEnvelope) hessian2Input.readObject();
		}
	}
}
