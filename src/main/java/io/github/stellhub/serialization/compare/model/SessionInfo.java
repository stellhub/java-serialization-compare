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
public class SessionInfo implements Serializable {

	private String sessionId;
	private String device;
	private String ip;
	private List<String> recentActions;
}
