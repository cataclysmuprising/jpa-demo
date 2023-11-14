package com.example.persistence.entity;

import com.example.persistence.dto.AuthenticatedClientDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Entity
@DynamicUpdate
@Table(name = "mjr_administrator")
@Getter
@Setter
@ToString(callSuper = true)
public class Administrator extends AbstractEntity {

	@Column(name = "content_id")
	private Long contentId;

	@NotBlank
	@Length(max = 50)
	@Column(name = "name", nullable = false)
	private String name;

	@NotBlank
	@Length(max = 50)
	@Column(name = "login_id", nullable = false, unique = true)
	private String loginId;

	@NotBlank
	@Length(max = 200)
	@Column(name = "password", nullable = false)
	private String password;

	//@NotNull
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "status", nullable = false)
	private AuthenticatedClientDTO.Status status;

	@ManyToMany
	//@formatter:off
	@JoinTable(name = "mjr_administrator_x_role",
			joinColumns = { @JoinColumn(name = "administrator_id")},
			inverseJoinColumns = { @JoinColumn(name = "role_id") })
	//@formatter:on
	@ToString.Exclude
	private List<Role> roles;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "content_id", insertable = false, updatable = false)
	private StaticContent staticContent;
}
