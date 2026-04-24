package com.zelodesk.identity.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Testes de Autenticação (Login)")
public class AuthenticationTests {

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	@DisplayName("Login bem-sucedido com usuário comum")
	public void testLoginUsuarioComum() {
		// Arrange
		String email = "usuario@zelodesk.com";
		String senha = "123456";

		// Act
		var userDetails = userService.loadUserByUsername(email);

		// Assert
		assertNotNull(userDetails, "UserDetails não deve ser nulo");
		assertEquals(email, userDetails.getUsername(), "Email deve corresponder");
		assertTrue(passwordEncoder.matches(senha, userDetails.getPassword()),
				"Senha deve corresponder ao hash armazenado");
		assertTrue(userDetails.getAuthorities().stream()
				.anyMatch(a -> a.getAuthority().equals("ROLE_USUARIO")),
				"Deve ter role ROLE_USUARIO");
	}

	@Test
	@DisplayName("Login bem-sucedido com admin")
	public void testLoginAdmin() {
		// Arrange
		String email = "admin@zelodesk.com";
		String senha = "123456";

		// Act
		var userDetails = userService.loadUserByUsername(email);

		// Assert
		assertNotNull(userDetails, "UserDetails não deve ser nulo");
		assertEquals(email, userDetails.getUsername(), "Email deve corresponder");
		assertTrue(passwordEncoder.matches(senha, userDetails.getPassword()),
				"Senha deve corresponder ao hash armazenado");
		assertTrue(userDetails.getAuthorities().stream()
				.anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")),
				"Deve ter role ROLE_ADMIN");
	}

	@Test
	@DisplayName("Login com email inexistente deve lançar exceção")
	public void testLoginEmailInexistente() {
		// Arrange
		String emailInexistente = "naoexiste@zelodesk.com";

		// Act & Assert
		assertThrows(org.springframework.security.core.userdetails.UsernameNotFoundException.class, () -> {
			userService.loadUserByUsername(emailInexistente);
		}, "Deve lançar UsernameNotFoundException");
	}
}
