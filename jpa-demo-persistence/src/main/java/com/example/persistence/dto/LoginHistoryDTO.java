package com.example.persistence.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString(callSuper = true)
public class LoginHistoryDTO extends AbstractDTO {
	private long clientId;

	private AuthenticatedClientDTO.ClientType clientType;

	private String ipAddress;

	private String os;

	private String clientAgent;

	private LocalDateTime loginDate;
}
