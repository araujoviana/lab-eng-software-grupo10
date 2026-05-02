package com.zelodesk.identity.api;

import com.zelodesk.identity.application.UserService;
import com.zelodesk.identity.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/usuarios")
public class UsuarioController {

    @Autowired
    private UserService service;

	@PreAuthorize("hasAnyAuthority('ROLE_USUARIO','ROLE_ADMIN','ROLE_SOLICITANTE','ROLE_TRIAGEM','ROLE_EXECUTOR')")
	@GetMapping(value = "/{id}")
	public ResponseEntity<UserDTO> findById(@PathVariable Long id){
		UserDTO user = service.findById(id);
		return ResponseEntity.ok(user);
	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@GetMapping
	public ResponseEntity<Page<UserDTO>> findAll(Pageable pageable){
		Page<UserDTO> user = service.findAll(pageable);
		return ResponseEntity.ok(user);
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/me")
	public ResponseEntity<UserDTO> me(@AuthenticationPrincipal Jwt jwt) {
		UserDTO user = service.findByEmail(jwt.getClaimAsString("username"));
		return ResponseEntity.ok(user);
	}

	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_TRIAGEM')")
	@GetMapping(value = "/executores")
	public ResponseEntity<List<UserDTO>> findExecutores() {
		return ResponseEntity.ok(service.findExecutores());
	}
}
