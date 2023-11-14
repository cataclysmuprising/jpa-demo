package com.example.persistence.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class AdministratorRoleDTO extends AbstractDTO {

	private Long administratorId;

	private Long roleId;

	private AdministratorDTO administrator;

	private RoleDTO role;
}
