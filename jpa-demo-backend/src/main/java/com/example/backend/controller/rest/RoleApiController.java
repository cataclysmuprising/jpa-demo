package com.example.backend.controller.rest;

import com.example.backend.BackendApplication;
import com.example.persistence.criteria.RoleCriteria;
import com.example.persistence.dto.RoleDTO;
import com.example.persistence.mapper.utils.PaginatedResult;
import com.example.persistence.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sec/roles")
public class RoleApiController extends BaseRESTController {

	@Autowired
	private RoleService roleService;

	@PostMapping("/search/paging")
	public ResponseEntity<?> dataTableSearch(@RequestBody RoleCriteria criteria) {
		criteria.setAppName(BackendApplication.APP_NAME);
		PaginatedResult<RoleDTO> result = roleService.findAll(criteria, criteria.getPager());
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping("/search/list")
	public ResponseEntity<?> serachList(@RequestBody RoleCriteria criteria) {
		criteria.setAppName(BackendApplication.APP_NAME);
		List<RoleDTO> admins = roleService.findAll(criteria);
		return new ResponseEntity<>(admins, HttpStatus.OK);
	}
}
