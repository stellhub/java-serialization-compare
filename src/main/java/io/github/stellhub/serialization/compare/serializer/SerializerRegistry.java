package io.github.stellhub.serialization.compare.serializer;

import java.util.List;

public final class SerializerRegistry {

	private SerializerRegistry() {
	}

	/**
		 * 返回参与测试的全部序列化实现。
		 */
	public static List<SerializerEngine> createDefaultSerializers() {
		return List.of(
				new JdkSerializerEngine(),
				new JacksonJsonSerializerEngine(),
				new JacksonSmileSerializerEngine(),
				new ProtobufSerializerEngine(),
				new KryoSerializerEngine(),
				new Hessian2SerializerEngine());
	}
}
