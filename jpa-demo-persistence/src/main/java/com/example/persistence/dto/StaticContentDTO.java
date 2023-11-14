package com.example.persistence.dto;

import com.example.persistence.entity.StaticContent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class StaticContentDTO extends AbstractDTO {
	private String fileName;

	private String filePath;

	private String fileSize;

	private StaticContent.FileType fileType;
}
