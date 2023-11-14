package com.example.persistence.dto;

import com.example.persistence.entity.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
public class RoleDTO extends AbstractDTO {
	private String appName;

	private String name;

	private Role.RoleType roleType;

	private String description;

	private List<AdministratorDTO> administrators;

	private List<ActionDTO> actions;
}
