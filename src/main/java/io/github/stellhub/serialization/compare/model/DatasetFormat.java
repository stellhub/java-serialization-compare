package io.github.stellhub.serialization.compare.model;

public enum DatasetFormat {
	STRUCTURED_FLAT(DataNature.STRUCTURED, "结构化-平铺对象"),
	STRUCTURED_NESTED(DataNature.STRUCTURED, "结构化-嵌套对象"),
	UNSTRUCTURED_TEXT(DataNature.UNSTRUCTURED, "非结构化-文本"),
	UNSTRUCTURED_BINARY(DataNature.UNSTRUCTURED, "非结构化-二进制");

	private final DataNature nature;
	private final String displayName;

	DatasetFormat(DataNature nature, String displayName) {
		this.nature = nature;
		this.displayName = displayName;
	}

	public DataNature getNature() {
		return nature;
	}

	public String getDisplayName() {
		return displayName;
	}
}
