package com.example.persistence.dto;

import com.example.persistence.entity.Action;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
public class ActionDTO extends AbstractDTO {
	private String appName;

	private String page;

	private String actionName;

	private String displayName;

	private Action.ActionType actionType;

	private String url;

	private String description;

	private List<RoleDTO> roles;
}
