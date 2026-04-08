package com.zelodesk.services;

import com.zelodesk.dtos.UserDTO;
import com.zelodesk.entities.Role;
import com.zelodesk.projections.UserDetailsProjection;
import com.zelodesk.entities.Usuario;
import com.zelodesk.repositories.UsuarioRepository;
import com.zelodesk.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
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

	public Page<UserDTO> findAll(Pageable pageable){
		Page<Usuario> user = repository.findAll(pageable);
		return user.map(x -> new UserDTO(x));

	}
}
