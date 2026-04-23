package io.github.stellhub.serialization.compare.protobuf;

import com.google.protobuf.ByteString;
import io.github.stellhub.serialization.compare.model.Address;
import io.github.stellhub.serialization.compare.model.BenchmarkEnvelope;
import io.github.stellhub.serialization.compare.model.BinaryChunk;
import io.github.stellhub.serialization.compare.model.DataNature;
import io.github.stellhub.serialization.compare.model.DatasetFormat;
import io.github.stellhub.serialization.compare.model.DatasetSize;
import io.github.stellhub.serialization.compare.model.Order;
import io.github.stellhub.serialization.compare.model.OrderItem;
import io.github.stellhub.serialization.compare.model.SessionInfo;
import io.github.stellhub.serialization.compare.model.StructuredFlatPayload;
import io.github.stellhub.serialization.compare.model.StructuredNestedPayload;
import io.github.stellhub.serialization.compare.model.UnstructuredBinaryPayload;
import io.github.stellhub.serialization.compare.model.UnstructuredTextPayload;
import io.github.stellhub.serialization.compare.model.UserProfile;
import java.util.List;

public class ProtobufMapper {

	/**
		 * 将统一模型转换为 protobuf 模型。
		 */
	public BenchmarkEnvelopeProto toProto(BenchmarkEnvelope envelope) {
		BenchmarkEnvelopeProto.Builder builder = BenchmarkEnvelopeProto.newBuilder()
				.setScenarioCode(envelope.getScenarioCode())
				.setDatasetSize(toProtoSize(envelope.getDatasetSize()))
				.setDatasetFormat(toProtoFormat(envelope.getDatasetFormat()))
				.setDataNature(toProtoNature(envelope.getDataNature()))
				.setSeed(envelope.getSeed());

		if (envelope.getStructuredFlatPayload() != null) {
			builder.setStructuredFlatPayload(toProto(envelope.getStructuredFlatPayload()));
		}
		if (envelope.getStructuredNestedPayload() != null) {
			builder.setStructuredNestedPayload(toProto(envelope.getStructuredNestedPayload()));
		}
		if (envelope.getUnstructuredTextPayload() != null) {
			builder.setUnstructuredTextPayload(toProto(envelope.getUnstructuredTextPayload()));
		}
		if (envelope.getUnstructuredBinaryPayload() != null) {
			builder.setUnstructuredBinaryPayload(toProto(envelope.getUnstructuredBinaryPayload()));
		}
		return builder.build();
	}

	/**
		 * 将 protobuf 模型转换为统一模型。
		 */
	public BenchmarkEnvelope fromProto(BenchmarkEnvelopeProto proto) {
		BenchmarkEnvelope.BenchmarkEnvelopeBuilder builder = BenchmarkEnvelope.builder()
				.scenarioCode(proto.getScenarioCode())
				.datasetSize(fromProtoSize(proto.getDatasetSize()))
				.datasetFormat(fromProtoFormat(proto.getDatasetFormat()))
				.dataNature(fromProtoNature(proto.getDataNature()))
				.seed(proto.getSeed());

		switch (proto.getPayloadCase()) {
			case STRUCTURED_FLAT_PAYLOAD -> builder.structuredFlatPayload(fromProto(proto.getStructuredFlatPayload()));
			case STRUCTURED_NESTED_PAYLOAD -> builder.structuredNestedPayload(fromProto(proto.getStructuredNestedPayload()));
			case UNSTRUCTURED_TEXT_PAYLOAD -> builder.unstructuredTextPayload(fromProto(proto.getUnstructuredTextPayload()));
			case UNSTRUCTURED_BINARY_PAYLOAD -> builder.unstructuredBinaryPayload(fromProto(proto.getUnstructuredBinaryPayload()));
			case PAYLOAD_NOT_SET -> {
			}
		}
		return builder.build();
	}

	private StructuredFlatPayloadProto toProto(StructuredFlatPayload payload) {
		return StructuredFlatPayloadProto.newBuilder()
				.setRecordId(payload.getRecordId())
				.setTenantId(payload.getTenantId())
				.setSequence(payload.getSequence())
				.setAmount(payload.getAmount())
				.setActive(payload.isActive())
				.addAllTags(payload.getTags())
				.putAllAttributes(payload.getAttributes())
				.addAllScores(payload.getScores())
				.setDescription(payload.getDescription())
				.build();
	}

	private StructuredFlatPayload fromProto(StructuredFlatPayloadProto proto) {
		return StructuredFlatPayload.builder()
				.recordId(proto.getRecordId())
				.tenantId(proto.getTenantId())
				.sequence(proto.getSequence())
				.amount(proto.getAmount())
				.active(proto.getActive())
				.tags(proto.getTagsList())
				.attributes(proto.getAttributesMap())
				.scores(proto.getScoresList())
				.description(proto.getDescription())
				.build();
	}

	private StructuredNestedPayloadProto toProto(StructuredNestedPayload payload) {
		StructuredNestedPayloadProto.Builder builder = StructuredNestedPayloadProto.newBuilder()
				.setUser(toProto(payload.getUser()))
				.putAllLabels(payload.getLabels())
				.setSummary(payload.getSummary());
		for (Order order : payload.getOrders()) {
			builder.addOrders(toProto(order));
		}
		for (SessionInfo session : payload.getSessions()) {
			builder.addSessions(toProto(session));
		}
		return builder.build();
	}

	private StructuredNestedPayload fromProto(StructuredNestedPayloadProto proto) {
		return StructuredNestedPayload.builder()
				.user(fromProto(proto.getUser()))
				.orders(proto.getOrdersList().stream().map(this::fromProto).toList())
				.sessions(proto.getSessionsList().stream().map(this::fromProto).toList())
				.labels(proto.getLabelsMap())
				.summary(proto.getSummary())
				.build();
	}

	private UserProfileProto toProto(UserProfile payload) {
		return UserProfileProto.newBuilder()
				.setUserId(payload.getUserId())
				.setAccount(payload.getAccount())
				.setDisplayName(payload.getDisplayName())
				.setAge(payload.getAge())
				.addAllInterests(payload.getInterests())
				.setPrimaryAddress(toProto(payload.getPrimaryAddress()))
				.build();
	}

	private UserProfile fromProto(UserProfileProto proto) {
		return UserProfile.builder()
				.userId(proto.getUserId())
				.account(proto.getAccount())
				.displayName(proto.getDisplayName())
				.age(proto.getAge())
				.interests(proto.getInterestsList())
				.primaryAddress(fromProto(proto.getPrimaryAddress()))
				.build();
	}

	private AddressProto toProto(Address payload) {
		return AddressProto.newBuilder()
				.setCountry(payload.getCountry())
				.setProvince(payload.getProvince())
				.setCity(payload.getCity())
				.setDistrict(payload.getDistrict())
				.setStreet(payload.getStreet())
				.setPostalCode(payload.getPostalCode())
				.build();
	}

	private Address fromProto(AddressProto proto) {
		return Address.builder()
				.country(proto.getCountry())
				.province(proto.getProvince())
				.city(proto.getCity())
				.district(proto.getDistrict())
				.street(proto.getStreet())
				.postalCode(proto.getPostalCode())
				.build();
	}

	private OrderProto toProto(Order payload) {
		OrderProto.Builder builder = OrderProto.newBuilder()
				.setOrderId(payload.getOrderId())
				.setCreatedAtEpochMillis(payload.getCreatedAtEpochMillis())
				.setTotalAmount(payload.getTotalAmount())
				.putAllAttributes(payload.getAttributes());
		for (OrderItem item : payload.getItems()) {
			builder.addItems(toProto(item));
		}
		return builder.build();
	}

	private Order fromProto(OrderProto proto) {
		return Order.builder()
				.orderId(proto.getOrderId())
				.createdAtEpochMillis(proto.getCreatedAtEpochMillis())
				.items(proto.getItemsList().stream().map(this::fromProto).toList())
				.totalAmount(proto.getTotalAmount())
				.attributes(proto.getAttributesMap())
				.build();
	}

	private OrderItemProto toProto(OrderItem payload) {
		return OrderItemProto.newBuilder()
				.setSku(payload.getSku())
				.setName(payload.getName())
				.setQuantity(payload.getQuantity())
				.setPrice(payload.getPrice())
				.build();
	}

	private OrderItem fromProto(OrderItemProto proto) {
		return OrderItem.builder()
				.sku(proto.getSku())
				.name(proto.getName())
				.quantity(proto.getQuantity())
				.price(proto.getPrice())
				.build();
	}

	private SessionProto toProto(SessionInfo payload) {
		return SessionProto.newBuilder()
				.setSessionId(payload.getSessionId())
				.setDevice(payload.getDevice())
				.setIp(payload.getIp())
				.addAllRecentActions(payload.getRecentActions())
				.build();
	}

	private SessionInfo fromProto(SessionProto proto) {
		return SessionInfo.builder()
				.sessionId(proto.getSessionId())
				.device(proto.getDevice())
				.ip(proto.getIp())
				.recentActions(proto.getRecentActionsList())
				.build();
	}

	private UnstructuredTextPayloadProto toProto(UnstructuredTextPayload payload) {
		return UnstructuredTextPayloadProto.newBuilder()
				.setDocumentId(payload.getDocumentId())
				.setTitle(payload.getTitle())
				.setLanguage(payload.getLanguage())
				.addAllKeywords(payload.getKeywords())
				.setRawText(payload.getRawText())
				.setRawJsonFragment(payload.getRawJsonFragment())
				.putAllMetadata(payload.getMetadata())
				.build();
	}

	private UnstructuredTextPayload fromProto(UnstructuredTextPayloadProto proto) {
		return UnstructuredTextPayload.builder()
				.documentId(proto.getDocumentId())
				.title(proto.getTitle())
				.language(proto.getLanguage())
				.keywords(proto.getKeywordsList())
				.rawText(proto.getRawText())
				.rawJsonFragment(proto.getRawJsonFragment())
				.metadata(proto.getMetadataMap())
				.build();
	}

	private UnstructuredBinaryPayloadProto toProto(UnstructuredBinaryPayload payload) {
		UnstructuredBinaryPayloadProto.Builder builder = UnstructuredBinaryPayloadProto.newBuilder()
				.setResourceId(payload.getResourceId())
				.setMediaType(payload.getMediaType())
				.setContent(ByteString.copyFrom(payload.getContent()))
				.putAllMetadata(payload.getMetadata())
				.setChecksum(payload.getChecksum());
		for (BinaryChunk chunk : payload.getChunks()) {
			builder.addChunks(ByteString.copyFrom(chunk.getData()));
		}
		return builder.build();
	}

	private UnstructuredBinaryPayload fromProto(UnstructuredBinaryPayloadProto proto) {
		List<BinaryChunk> chunks = proto.getChunksList().stream()
				.map(item -> BinaryChunk.builder().data(item.toByteArray()).build())
				.toList();
		return UnstructuredBinaryPayload.builder()
				.resourceId(proto.getResourceId())
				.mediaType(proto.getMediaType())
				.content(proto.getContent().toByteArray())
				.chunks(chunks)
				.metadata(proto.getMetadataMap())
				.checksum(proto.getChecksum())
				.build();
	}

	private DatasetSizeProto toProtoSize(DatasetSize size) {
		return switch (size) {
			case SMALL -> DatasetSizeProto.DATASET_SIZE_PROTO_SMALL;
			case MEDIUM -> DatasetSizeProto.DATASET_SIZE_PROTO_MEDIUM;
			case LARGE -> DatasetSizeProto.DATASET_SIZE_PROTO_LARGE;
		};
	}

	private DatasetSize fromProtoSize(DatasetSizeProto size) {
		return switch (size) {
			case DATASET_SIZE_PROTO_SMALL -> DatasetSize.SMALL;
			case DATASET_SIZE_PROTO_MEDIUM -> DatasetSize.MEDIUM;
			case DATASET_SIZE_PROTO_LARGE, DATASET_SIZE_PROTO_UNSPECIFIED, UNRECOGNIZED -> DatasetSize.LARGE;
		};
	}

	private DatasetFormatProto toProtoFormat(DatasetFormat format) {
		return switch (format) {
			case STRUCTURED_FLAT -> DatasetFormatProto.DATASET_FORMAT_PROTO_STRUCTURED_FLAT;
			case STRUCTURED_NESTED -> DatasetFormatProto.DATASET_FORMAT_PROTO_STRUCTURED_NESTED;
			case UNSTRUCTURED_TEXT -> DatasetFormatProto.DATASET_FORMAT_PROTO_UNSTRUCTURED_TEXT;
			case UNSTRUCTURED_BINARY -> DatasetFormatProto.DATASET_FORMAT_PROTO_UNSTRUCTURED_BINARY;
		};
	}

	private DatasetFormat fromProtoFormat(DatasetFormatProto format) {
		return switch (format) {
			case DATASET_FORMAT_PROTO_STRUCTURED_FLAT -> DatasetFormat.STRUCTURED_FLAT;
			case DATASET_FORMAT_PROTO_STRUCTURED_NESTED -> DatasetFormat.STRUCTURED_NESTED;
			case DATASET_FORMAT_PROTO_UNSTRUCTURED_TEXT -> DatasetFormat.UNSTRUCTURED_TEXT;
			case DATASET_FORMAT_PROTO_UNSTRUCTURED_BINARY, DATASET_FORMAT_PROTO_UNSPECIFIED, UNRECOGNIZED ->
					DatasetFormat.UNSTRUCTURED_BINARY;
		};
	}

	private DataNatureProto toProtoNature(DataNature nature) {
		return nature == DataNature.STRUCTURED
				? DataNatureProto.DATA_NATURE_PROTO_STRUCTURED
				: DataNatureProto.DATA_NATURE_PROTO_UNSTRUCTURED;
	}

	private DataNature fromProtoNature(DataNatureProto nature) {
		return nature == DataNatureProto.DATA_NATURE_PROTO_STRUCTURED ? DataNature.STRUCTURED : DataNature.UNSTRUCTURED;
	}
}
