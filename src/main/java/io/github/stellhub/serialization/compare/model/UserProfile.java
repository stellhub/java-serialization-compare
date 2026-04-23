package io.github.stellhub.serialization.compare.model;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile implements Serializable {

	private String userId;
	private String account;
	private String displayName;
	private int age;
	private List<String> interests;
	private Address primaryAddress;
}
