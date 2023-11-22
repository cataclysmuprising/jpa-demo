package com.example.backend.controller.rest;

import com.example.persistence.criteria.AdministratorCriteria;
import com.example.persistence.dto.AdministratorDTO;
import com.example.persistence.mapper.utils.PaginatedResult;
import com.example.persistence.service.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sec/admins")
public class AdministratorApiController extends BaseRESTController {

	@Autowired
	private AdministratorService administratorService;

	@PostMapping("/search/paging")
	public ResponseEntity<?> dataTableSearch(@RequestBody AdministratorCriteria criteria) {
		PaginatedResult<AdministratorDTO> result = administratorService.findAll(criteria, criteria.getPager());
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping("/search/list")
	public ResponseEntity<?> serachList(@RequestBody AdministratorCriteria criteria) {
		List<AdministratorDTO> admins = administratorService.findAll(criteria);
		return new ResponseEntity<>(admins, HttpStatus.OK);
	}
}
