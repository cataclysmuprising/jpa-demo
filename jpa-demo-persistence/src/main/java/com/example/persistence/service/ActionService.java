package com.example.persistence.service;

import com.example.persistence.config.PrimaryPersistenceContext;
import com.example.persistence.criteria.ActionCriteria;
import com.example.persistence.criteria.RoleCriteria;
import com.example.persistence.dto.ActionDTO;
import com.example.persistence.entity.Action;
import com.example.persistence.exception.BusinessException;
import com.example.persistence.mapper.ActionMapper;
import com.example.persistence.repository.ActionRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@Transactional(transactionManager = PrimaryPersistenceContext.TX_MANAGER, rollbackFor = Exception.class)
public class ActionService extends BaseService<Action, ActionCriteria, ActionDTO, ActionMapper> {

	private static final Logger serviceLogger = LogManager.getLogger("serviceLogs." + ActionService.class);
	private final ActionRepository repository;
	private final ActionMapper mapper;

	@Autowired
	ActionService(ActionRepository repository, ActionMapper mapper) {
		super(repository, mapper);
		this.repository = repository;
		this.mapper = mapper;
	}

	public List<String> selectAvailableActionsForUser(String page, String appName, List<Long> roleIds) throws BusinessException {
		try {
			List<String> availableActions = new ArrayList<>();

			ActionCriteria criteria = new ActionCriteria();
			criteria.setActionType(Action.ActionType.MAIN);
			criteria.setAppName(appName);

			RoleCriteria roleCriteria = new RoleCriteria();
			roleCriteria.setIncludeIds(new HashSet<>(roleIds));
			criteria.setRole(roleCriteria);
			List<Action> mainPageActions = repository.findAll(criteria.getFilter(), "Action(roles)");
			if (!CollectionUtils.isEmpty(mainPageActions)) {
				mainPageActions.forEach(action -> availableActions.add(action.getActionName()));
			}

			if (StringUtils.isNotBlank(page)) {
				criteria.setPage(page);
				criteria.setActionType(Action.ActionType.SUB);

				List<Action> subPageActions = repository.findAll(criteria.getFilter(), "Action(roles)");
				if (!CollectionUtils.isEmpty(subPageActions)) {
					subPageActions.forEach(action -> availableActions.add(action.getActionName()));
				}
			}

			return availableActions.size() > 0 ? availableActions : null;
		}
		catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
	}
}
