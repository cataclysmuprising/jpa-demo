package com.example.persistence.mapper.utils;

import com.example.persistence.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperFactory {

	public static AdministratorMapper administratorMapper;

	public static RoleMapper roleMapper;

	public static StaticContentMapper staticContentMapper;

	public static ActionMapper actionMapper;

	public static AdministratorRoleMapper administratorRoleMapper;

	public static RoleActionMapper roleActionMapper;

	@Autowired
	public void setActionMapper(ActionMapper actionMapper) {
		MapperFactory.actionMapper = actionMapper;
	}

	@Autowired
	public void setAdministratorMapper(AdministratorMapper administratorMapper) {
		MapperFactory.administratorMapper = administratorMapper;
	}

	@Autowired
	public void setRoleMapper(RoleMapper roleMapper) {
		MapperFactory.roleMapper = roleMapper;
	}

	@Autowired
	public void setStaticContentMapper(StaticContentMapper staticContentMapper) {
		MapperFactory.staticContentMapper = staticContentMapper;
	}

	@Autowired
	public void setAdministratorRoleMapper(AdministratorRoleMapper administratorRoleMapper) {
		MapperFactory.administratorRoleMapper = administratorRoleMapper;
	}

	@Autowired
	public void setRoleActionMapper(RoleActionMapper roleActionMapper) {
		MapperFactory.roleActionMapper = roleActionMapper;
	}
}
