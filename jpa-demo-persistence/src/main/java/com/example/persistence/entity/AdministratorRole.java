package com.example.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "mjr_administrator_x_role")
@Getter
@Setter
@ToString(callSuper = true)
public class AdministratorRole extends AbstractEntity {

	@NotNull
	@Column(name = "administrator_id", nullable = false)
	private Long administratorId;

	@NotNull
	@Column(name = "role_id", nullable = false)
	private Long roleId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "administrator_id", insertable = false, updatable = false)
	private Administrator administrator;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", insertable = false, updatable = false)
	private Role role;

	public AdministratorRole(Long administratorId, Long roleId) {
		this.administratorId = administratorId;
		this.roleId = roleId;
	}
}
