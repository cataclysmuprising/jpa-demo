package com.example.persistence.service;

import com.example.persistence.config.PrimaryPersistenceContext;
import com.example.persistence.criteria.StaticContentCriteria;
import com.example.persistence.dto.StaticContentDTO;
import com.example.persistence.entity.StaticContent;
import com.example.persistence.mapper.StaticContentMapper;
import com.example.persistence.repository.StaticContentRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(transactionManager = PrimaryPersistenceContext.TX_MANAGER, rollbackFor = Exception.class)
public class StaticContentService extends BaseService<StaticContent, StaticContentCriteria, StaticContentDTO, StaticContentMapper> {

	private static final Logger serviceLogger = LogManager.getLogger("serviceLogs." + StaticContentService.class);
	private final StaticContentRepository repository;
	private final StaticContentMapper mapper;

	@Autowired
	StaticContentService(StaticContentRepository repository, StaticContentMapper mapper) {
		super(repository, mapper);
		this.repository = repository;
		this.mapper = mapper;
	}
}
