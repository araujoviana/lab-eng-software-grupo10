package com.zelodesk.identity.repository;

import com.zelodesk.identity.domain.Usuario;
import com.zelodesk.identity.projection.UserDetailsProjection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Testes do UsuarioRepository")
public class UsuarioRepositoryTests {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Test
	@DisplayName("Deve buscar usuário por ID")
	public void testFindById() {
		Optional<Usuario> usuario = usuarioRepository.findById(2L);

		assertTrue(usuario.isPresent(), "Usuário deve ser encontrado");
		assertEquals("usuario@zelodesk.com", usuario.get().getEmail());
		assertEquals("Usuario Teste", usuario.get().getNome());
	}

	@Test
	@DisplayName("Deve retornar vazio ao buscar ID inexistente")
	public void testFindByIdNotFound() {
		Optional<Usuario> usuario = usuarioRepository.findById(999L);

		assertFalse(usuario.isPresent(), "Usuário não deve ser encontrado");
	}

	@Test
	@DisplayName("Deve buscar usuário e roles por email")
	public void testSearchUserAndRolesByEmail() {
		List<UserDetailsProjection> projections = usuarioRepository.searchUserAndRolesByEmail("usuario@zelodesk.com");

		assertNotNull(projections, "Lista não deve ser nula");
		assertFalse(projections.isEmpty(), "Deve encontrar o usuário");
		assertEquals("usuario@zelodesk.com", projections.get(0).getEmail());
		assertEquals("ROLE_USUARIO", projections.get(0).getAuthority());
	}

	@Test
	@DisplayName("Deve buscar admin e suas roles por email")
	public void testSearchAdminAndRolesByEmail() {
		List<UserDetailsProjection> projections = usuarioRepository.searchUserAndRolesByEmail("admin@zelodesk.com");

		assertNotNull(projections, "Lista não deve ser nula");
		assertFalse(projections.isEmpty(), "Deve encontrar o admin");
		assertEquals("admin@zelodesk.com", projections.get(0).getEmail());
		assertEquals("ROLE_ADMIN", projections.get(0).getAuthority());
	}

	@Test
	@DisplayName("Deve retornar lista vazia para email inexistente")
	public void testSearchUserByEmailNotFound() {
		List<UserDetailsProjection> projections = usuarioRepository.searchUserAndRolesByEmail("naoexiste@zelodesk.com");

		assertNotNull(projections, "Lista não deve ser nula");
		assertTrue(projections.isEmpty(), "Deve retornar lista vazia");
	}

	@Test
	@DisplayName("Deve listar todos os usuários")
	public void testFindAll() {
		var usuarios = usuarioRepository.findAll();

		assertNotNull(usuarios, "Lista não deve ser nula");
		assertTrue(usuarios.size() >= 4, "Deve ter pelo menos 4 usuários");
	}

	@Test
	@DisplayName("Deve encontrar Carlos Silva por ID")
	public void testFindCarlosSilvaById() {
		Optional<Usuario> usuario = usuarioRepository.findById(3L);

		assertTrue(usuario.isPresent(), "Carlos Silva deve ser encontrado");
		assertEquals("carlos@zelodesk.com", usuario.get().getEmail());
		assertEquals("Carlos Silva", usuario.get().getNome());
	}

	@Test
	@DisplayName("Deve encontrar Ana Souza por ID")
	public void testFindAnaSouzaById() {
		Optional<Usuario> usuario = usuarioRepository.findById(4L);

		assertTrue(usuario.isPresent(), "Ana Souza deve ser encontrada");
		assertEquals("ana@zelodesk.com", usuario.get().getEmail());
		assertEquals("Ana Souza", usuario.get().getNome());
	}

	@Test
	@DisplayName("Deve buscar Carlos Silva por email com role")
	public void testSearchCarlosSilvaByEmail() {
		List<UserDetailsProjection> projections = usuarioRepository.searchUserAndRolesByEmail("carlos@zelodesk.com");

		assertNotNull(projections, "Lista não deve ser nula");
		assertFalse(projections.isEmpty(), "Deve encontrar Carlos Silva");
		assertEquals("ROLE_USUARIO", projections.get(0).getAuthority());
	}

	@Test
	@DisplayName("Deve buscar Ana Souza por email com role")
	public void testSearchAnaSouzaByEmail() {
		List<UserDetailsProjection> projections = usuarioRepository.searchUserAndRolesByEmail("ana@zelodesk.com");

		assertNotNull(projections, "Lista não deve ser nula");
		assertFalse(projections.isEmpty(), "Deve encontrar Ana Souza");
		assertEquals("ROLE_USUARIO", projections.get(0).getAuthority());
	}

	@Test
	@DisplayName("Usuário tem role definida")
	public void testUsuarioHasRole() {
		Optional<Usuario> usuario = usuarioRepository.findById(2L);

		assertTrue(usuario.isPresent(), "Usuário deve ser encontrado");
		assertFalse(usuario.get().getRoles().isEmpty(), "Usuário deve ter roles");
		assertTrue(usuario.get().hasRole("ROLE_USUARIO"), "Deve ter ROLE_USUARIO");
	}

	@Test
	@DisplayName("Admin deve ter role ROLE_ADMIN")
	public void testAdminHasAdminRole() {
		Optional<Usuario> usuario = usuarioRepository.findById(1L);

		assertTrue(usuario.isPresent(), "Admin deve ser encontrado");
		assertFalse(usuario.get().getRoles().isEmpty(), "Admin deve ter roles");
		assertTrue(usuario.get().hasRole("ROLE_ADMIN"), "Deve ter ROLE_ADMIN");
	}
}
