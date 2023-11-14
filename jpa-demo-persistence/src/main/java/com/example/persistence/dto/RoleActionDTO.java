package com.example.persistence.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class RoleActionDTO extends AbstractDTO {

	private Long roleId;

	private Long actionId;

	private RoleDTO role;

	private ActionDTO action;
}
