package io.github.stellhub.serialization.compare.model;

public enum DatasetSize {
	SMALL("小"),
	MEDIUM("中"),
	LARGE("大");

	private final String displayName;

	DatasetSize(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
}
