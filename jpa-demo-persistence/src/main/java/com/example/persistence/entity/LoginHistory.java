package com.example.persistence.entity;

import com.example.persistence.dto.AuthenticatedClientDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Entity
@Table(name = "mjr_login_history")
@Getter
@Setter
@ToString(callSuper = true)
public class LoginHistory extends AbstractEntity {

	@Column(name = "client_id")
	private long clientId;

	@NotNull
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "client_type", nullable = false)
	private AuthenticatedClientDTO.ClientType clientType;

	@NotBlank
	@Length(max = 45)
	@Column(name = "ip_address", nullable = false)
	private String ipAddress;

	@Length(max = 100)
	@Column(name = "os")
	private String os;

	@Length(max = 100)
	@Column(name = "client_agent")
	private String clientAgent;

	@NotNull
	@Column(name = "login_date", nullable = false)
	private LocalDateTime loginDate;
}
