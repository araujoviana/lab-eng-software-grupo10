package com.zelodesk.identity.application;

import com.zelodesk.identity.domain.Role;
import com.zelodesk.identity.domain.Usuario;
import com.zelodesk.identity.dto.UserDTO;
import com.zelodesk.identity.projection.UserDetailsProjection;
import com.zelodesk.identity.repository.UsuarioRepository;
import com.zelodesk.shared.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UsuarioRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		List<UserDetailsProjection> result = repository.searchUserAndRolesByEmail(username);
		if (result.size() == 0) {
			throw new UsernameNotFoundException("Email not found");
		}
		
		Usuario user = new Usuario();
		user.setEmail(result.get(0).getEmail());
		user.setSenha(result.get(0).getSenha());
		for (UserDetailsProjection projection : result) {
			user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
		}
		
		return user;
	}

	public UserDTO findById(@PathVariable Long id){
		Usuario user =repository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Recurso não encontrado"));
		return new UserDTO(user);
	}

	public UserDTO findByEmail(String email){
		Usuario user = repository.findByEmail(email).orElseThrow(
				() -> new ResourceNotFoundException("Usuario nao encontrado"));
		return new UserDTO(user);
	}

	public Page<UserDTO> findAll(Pageable pageable){
		Page<Usuario> user = repository.findAll(pageable);
		return user.map(x -> new UserDTO(x));

	}

	public List<UserDTO> findExecutores(){
		return repository.findByRoleAuthority("ROLE_EXECUTOR").stream()
				.map(UserDTO::new)
				.toList();
	}
}
