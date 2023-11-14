package com.example.persistence.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class SettingDTO extends AbstractDTO {
	private String group;

	private String subGroup;

	private String type;

	private String name;

	private String value;
}
