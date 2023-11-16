package com.example.backend.config.security;

import com.example.persistence.dto.AuthenticatedClientDTO;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AuthenticatedClient implements UserDetails {
	@Serial
	private static final long serialVersionUID = 1774247443506921090L;
	private AuthenticatedClientDTO client;
	private List<String> roles;
	private String requestUrl;

	public AuthenticatedClient(AuthenticatedClientDTO client, List<String> roles) {
		this.client = client;
		this.roles = roles;
	}

	@JsonDeserialize(using = CustomAuthorityDeserializer.class)
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		for (String role : roles) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		return authorities;
	}

	@Override
	public String getPassword() {
		return client.getPassword();
	}

	@Override
	public String getUsername() {
		return client.getLoginId();
	}

	public Long getId() {
		return client.getId();
	}

	public String getLoginId() {
		return client.getLoginId();
	}

	public String getClientName() {
		return client.getName();
	}

	public AuthenticatedClientDTO.ClientType getClientType() {
		return client.getClientType();
	}

	public AuthenticatedClientDTO getUserDetail() {
		return client;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return client.getStatus() != AuthenticatedClientDTO.Status.LOCKED;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return client.getStatus() == AuthenticatedClientDTO.Status.ACTIVE;
	}
}
