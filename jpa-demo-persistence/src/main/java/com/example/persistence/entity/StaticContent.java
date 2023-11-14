package com.example.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "mjr_static_content")
@Getter
@Setter
@ToString(callSuper = true)
public class StaticContent extends AbstractEntity {

	@NotBlank
	@Length(max = 200)
	@Column(name = "file_name", nullable = false)
	private String fileName;

	@NotBlank
	@Column(name = "file_path", nullable = false)
	private String filePath;

	@NotBlank
	@Length(max = 20)
	@Column(name = "file_size", nullable = false)
	private String fileSize;

	@NotNull
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "file_type", nullable = false)
	private FileType fileType;

	public enum FileType {
		IMAGE, TEXT, PDF, EXCEL, ZIP, UNKNOWN
	}
}
