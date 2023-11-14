package com.example.persistence.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
public class AuthenticatedClientDTO extends AbstractDTO {

	private static final long serialVersionUID = 9102219677959520473L;
	private String name;
	private String loginId;
	private String password;
	private ClientType clientType;
	private String nrc;
	private String since;
	private Long contentId;
	private Status status;
	private List<Long> roleIds;
	private List<String> roles;

	public enum Status {
		TEMPORARY, ACTIVE, LOCKED, VERIFICATION_NEEDED
	}

	public enum ClientType {
		ADMINISTRATOR, USER
	}
}
