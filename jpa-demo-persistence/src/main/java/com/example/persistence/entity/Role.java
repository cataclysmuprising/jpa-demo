package com.example.persistence.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Entity
@Table(name = "mjr_role")
@Getter
@Setter
@ToString(callSuper = true)
public class Role extends AbstractEntity {
	@NotBlank
	@Length(max = 30)
	@Column(name = "app_name", nullable = false)
	private String appName;

	@NotBlank
	@Length(max = 20)
	@Column(name = "name", nullable = false)
	private String name;

	@NotNull
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "type", nullable = false)
	private RoleType roleType;

	@NotBlank
	@Length(max = 200)
	@Column(name = "description", nullable = false)
	private String description;

	@ManyToMany(mappedBy = "roles")
	@ToString.Exclude
	private List<Administrator> administrators;

	@ManyToMany
	@ToString.Exclude
	//@formatter:off
	@JoinTable(name = "mjr_role_x_action",
			joinColumns = { @JoinColumn(name = "role_id") },
			inverseJoinColumns = { @JoinColumn(name = "action_id") })
	//@formatter:on
	private List<Action> actions;

	public enum RoleType {
		SYSTEM("system"), BUILT_IN("built-in"), CUSTOM("custom");

		private final String definition;

		RoleType(String definition) {
			this.definition = definition;
		}

		public static RoleType getEnum(String value) {
			RoleType _enum = null;
			RoleType[] var2 = values();
			int var3 = var2.length;

			for (RoleType v : var2) {
				if (v.getDefinition().trim().equalsIgnoreCase(value)) {
					_enum = v;
					break;
				}
			}

			return _enum;
		}

		@JsonValue
		public String getDefinition() {
			return definition;
		}

		@Override
		public String toString() {
			return definition;
		}
	}
}
