package io.github.stellhub.serialization.compare.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address implements Serializable {

	private String country;
	private String province;
	private String city;
	private String district;
	private String street;
	private String postalCode;
}
