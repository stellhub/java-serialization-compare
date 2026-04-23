package io.github.stellhub.serialization.compare.serializer;

import io.github.stellhub.serialization.compare.model.BenchmarkEnvelope;

public interface SerializerEngine {

	/**
		 * 返回序列化方案名称。
		 */
	String name();

	/**
		 * 将统一数据对象序列化为字节数组。
		 */
	byte[] serialize(BenchmarkEnvelope envelope) throws Exception;

	/**
		 * 将字节数组反序列化为统一数据对象。
		 */
	BenchmarkEnvelope deserialize(byte[] bytes) throws Exception;
}
