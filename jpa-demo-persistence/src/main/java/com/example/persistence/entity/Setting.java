package com.example.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "mjr_setting")
@Getter
@Setter
@ToString(callSuper = true)
public class Setting extends AbstractEntity {

	@NotBlank
	@Length(max = 50)
	@Column(name = "setting_group", nullable = false)
	private String group;

	@NotBlank
	@Length(max = 50)
	@Column(name = "setting_sub_group", nullable = false)
	private String subGroup;

	@NotBlank
	@Length(max = 50)
	@Column(name = "setting_type", nullable = false)
	private String type;

	@NotBlank
	@Length(max = 50)
	@Column(name = "setting_name", nullable = false)
	private String name;

	@NotBlank
	@Column(name = "setting_value", nullable = false)
	private String value;
}
