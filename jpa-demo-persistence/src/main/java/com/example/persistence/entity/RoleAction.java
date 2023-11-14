package com.example.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "mjr_role_x_action")
@Getter
@Setter
@ToString(callSuper = true)

public class RoleAction extends AbstractEntity {

	@NotNull
	@Column(name = "role_id", nullable = false)
	private Long roleId;

	@NotNull
	@Column(name = "action_id", nullable = false)
	private Long actionId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", insertable = false, updatable = false)
	private Role role;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "action_id", insertable = false, updatable = false)
	private Action action;
}
