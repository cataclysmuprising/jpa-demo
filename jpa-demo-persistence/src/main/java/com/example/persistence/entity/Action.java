package com.example.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Entity
@Table(name = "mjr_action")
@Getter
@Setter
@ToString(callSuper = true)
public class Action extends AbstractEntity {

	@NotBlank
	@Length(max = 30)
	@Column(name = "app_name", nullable = false)
	private String appName;

	@NotBlank
	@Length(max = 50)
	@Column(name = "page", nullable = false)
	private String page;

	@NotBlank
	@Length(max = 50)
	@Column(name = "action_name", nullable = false)
	private String actionName;

	@NotBlank
	@Length(max = 50)
	@Column(name = "display_name", nullable = false)
	private String displayName;

	@NotNull
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "action_type", nullable = false)
	private ActionType actionType;

	@NotBlank
	@Length(max = 250)
	@Column(name = "url", nullable = false, unique = true)
	private String url;

	@NotBlank
	@Length(max = 200)
	@Column(name = "description", nullable = false)
	private String description;

	@ManyToMany(mappedBy = "actions")
	@ToString.Exclude
	private List<Role> roles;

	public enum ActionType {
		MAIN, SUB
	}
}
