package com.zelodesk.identity.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Testes do UsuarioController")
public class UsuarioControllerTests {

	@Autowired
	private MockMvc mockMvc;

	private String tokenAdmin;
	private String tokenUsuario;

	@BeforeEach
	public void setup() throws Exception {
		tokenAdmin = obterToken("admin@zelodesk.com", "123456");
		tokenUsuario = obterToken("usuario@zelodesk.com", "123456");
	}

	private String obterToken(String email, String senha) throws Exception {
		String requestBody = "grant_type=password&username=" + email + "&password=" + senha
				+ "&scope=read%20write";

		// Client credentials via Basic Authentication header
		String clientAuth = java.util.Base64.getEncoder()
				.encodeToString("zelodesk-client:zelodesk-secret".getBytes());

		MvcResult result = mockMvc.perform(
				post("/oauth2/token")
						.header("Authorization", "Basic " + clientAuth)
						.contentType("application/x-www-form-urlencoded")
						.content(requestBody))
				.andExpect(status().isOk())
				.andReturn();

		String responseBody = result.getResponse().getContentAsString();
		com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
		com.fasterxml.jackson.databind.JsonNode node = mapper.readTree(responseBody);
		return node.get("access_token").asText();
	}

	@Test
	@DisplayName("Usuário deve conseguir buscar seu próprio ID")
	public void testFindByIdAsUsuario() throws Exception {
		mockMvc.perform(get("/usuarios/{id}", 2L)
				.header("Authorization", "Bearer " + tokenUsuario))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", equalTo(2)))
				.andExpect(jsonPath("$.email", equalTo("usuario@zelodesk.com")))
				.andExpect(jsonPath("$.nome", equalTo("Usuario Teste")));
	}

	@Test
	@DisplayName("Admin deve conseguir buscar qualquer usuário por ID")
	public void testFindByIdAsAdmin() throws Exception {
		mockMvc.perform(get("/usuarios/{id}", 2L)
				.header("Authorization", "Bearer " + tokenAdmin))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", equalTo(2)))
				.andExpect(jsonPath("$.email", equalTo("usuario@zelodesk.com")));
	}

	@Test
	@DisplayName("Admin deve conseguir listar todos os usuários")
	public void testFindAllAsAdmin() throws Exception {
		mockMvc.perform(get("/usuarios")
				.header("Authorization", "Bearer " + tokenAdmin))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content", hasSize(greaterThanOrEqualTo(2))))
				.andExpect(jsonPath("$.totalElements", greaterThanOrEqualTo(2)));
	}

	@Test
	@DisplayName("Usuário comum não deve conseguir listar todos (403 Forbidden)")
	public void testFindAllAsUsuarioForbidden() throws Exception {
		mockMvc.perform(get("/usuarios")
				.header("Authorization", "Bearer " + tokenUsuario))
				.andExpect(status().isForbidden());
	}
}
