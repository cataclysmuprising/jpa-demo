package com.example.persistence.service;

import com.example.persistence.criteria.AbstractCriteria;
import com.example.persistence.dto.AbstractDTO;
import com.example.persistence.entity.AbstractEntity;
import com.example.persistence.mapper.utils.EntityMapper;
import com.example.persistence.mapper.utils.MapperUtils;
import com.example.persistence.mapper.utils.PaginatedResult;
import com.example.persistence.repository.base.AbstractRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class BaseService<T extends AbstractEntity, C extends AbstractCriteria<?>, DTO extends AbstractDTO, M extends EntityMapper<DTO, T, C>> {

	private static final Logger serviceLogger = LogManager.getLogger("serviceLogs." + BaseService.class);
	private final AbstractRepository<T, C, Long> repository;
	private final M mapper;

	BaseService(AbstractRepository<T, C, Long> repository, M mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}

	@Transactional(readOnly = true)
	public DTO findById(long id) {
		serviceLogger.info("Finding single entry information by using id: <{}>", id);
		return mapper.toDTO(findEntryById(id), null);
	}

	@Transactional(readOnly = true)
	public DTO findOne(C criteria, String... hints) {
		serviceLogger.info("Finding single entry information by using criteria : <{}>", criteria);
		return mapper.toDTO(repository.findOne(criteria.getFilter(), hints).orElse(null), criteria);
	}

	@Transactional(readOnly = true)
	public List<DTO> findAll(C criteria, String... hints) {
		serviceLogger.info("Finding multiple entry informations by using criteria : <{}>", criteria);
		return mapper.toDTO(repository.findAll(criteria.getFilter(), hints), criteria);
	}

	@Transactional(readOnly = true)
	public PaginatedResult<DTO> findAll(C criteria, Pageable pager, String... hints) {
		serviceLogger.info("Finding multiple entry informations by using criteria : <{}>", criteria);
		Page<T> page = repository.findAll(criteria.getFilter(), pager, hints);
		List<DTO> entries = mapper.toDTO(page.getContent(), criteria);
		return MapperUtils.toPaginatedResult(page, entries);
	}

	public DTO create(DTO dto, long recordRegId) {
		serviceLogger.info("Creating a new entry by using information: <{}>", dto);
		T newEntity = mapper.toEntity(dto);
		newEntity.setRecordRegId(recordRegId);
		newEntity.setRecordUpdId(recordRegId);
		serviceLogger.info("Entity to create ==> " + newEntity);
		newEntity = repository.save(newEntity);
		return mapper.toDTO(newEntity, null);
	}

	public void createAll(Iterable<DTO> dtos, long recordRegId) {
		serviceLogger.info("Creating new entries by using information: <{}> ", dtos);
		List<T> entities = mapper.toEntity(dtos);
		entities.forEach(entity -> {
			entity.setRecordRegId(recordRegId);
			entity.setRecordUpdId(recordRegId);
		});
		repository.saveAll(entities);
	}

	public long update(DTO updateDTO, C criteria, long recordUpdId) {
		serviceLogger.info("Updating by criteria");
		T updateEntity = mapper.toEntity(updateDTO);
		updateEntity.setRecordUpdDate(LocalDateTime.now());
		updateEntity.setRecordUpdId(recordUpdId);
		serviceLogger.info("Update DTO => " + updateDTO);
		serviceLogger.info("Update Criteria => " + criteria);
		serviceLogger.info("Update Filter => " + criteria.getFilter());
		return repository.update(updateEntity, criteria);
	}

	public void deleteById(Long id) {
		serviceLogger.info("Deleting a administrator entry with id: <{}>", id);
		repository.deleteById(id);
	}

	public long delete(C criteria) {
		serviceLogger.info("Deleting multiple administrator entry with criteria: <{}>", criteria);
		return repository.deleteByCriteria(criteria);
	}

	protected T findEntryById(Long id) {
		Optional<T> result = repository.findById(id);
		return result.orElse(null);
	}
}
