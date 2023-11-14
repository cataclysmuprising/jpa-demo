package com.example.persistence.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
public class AdministratorDTO extends AbstractDTO {
	private Long contentId;

	private String name;

	private String loginId;

	@JsonIgnore
	private String password;

	private AuthenticatedClientDTO.Status status;

	private List<RoleDTO> roles;

	private StaticContentDTO staticContent;
}
