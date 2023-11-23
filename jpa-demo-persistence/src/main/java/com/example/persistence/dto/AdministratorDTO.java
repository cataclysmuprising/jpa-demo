package com.example.persistence.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString(callSuper = true)
public class AdministratorDTO extends AbstractDTO {
	private Long contentId;

	private String name;

	private String loginId;

	@JsonIgnore
	private String password;

	private String confirmPassword;

	private AuthenticatedClientDTO.Status status;

	private List<RoleDTO> roles;

	private Set<Long> roleIds;

	private StaticContentDTO staticContent;
}
