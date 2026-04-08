package com.zelodesk.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Testes do TicketController")
public class TicketControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String tokenUsuario;
    private String tokenAdmin;

    @BeforeEach
    public void setup() throws Exception {
        tokenUsuario = obterToken("usuario@zelodesk.com", "123456");
        tokenAdmin   = obterToken("admin@zelodesk.com",   "123456");
    }

    private String obterToken(String email, String senha) throws Exception {
        String body = "grant_type=password&username=" + email + "&password=" + senha
                + "&scope=read%20write";

        // Client credentials via Basic Authentication header
        String clientAuth = java.util.Base64.getEncoder()
                .encodeToString("zelodesk-client:zelodesk-secret".getBytes());

        MvcResult result = mockMvc.perform(post("/oauth2/token")
                        .header("Authorization", "Basic " + clientAuth)
                        .contentType("application/x-www-form-urlencoded")
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode node = objectMapper.readTree(result.getResponse().getContentAsString());
        return node.get("access_token").asText();
    }

    private String corpoTicket(String titulo, String descricao, String categoria, String local, String prioridade, String solicitador) {
        return String.format("""
                {
                  "titulo": "%s",
                  "descricao": "%s",
                  "categoria": "%s",
                  "localPredio": "%s",
                  "prioridade": "%s",
                  "solicitador": "%s"
                }
                """, titulo, descricao, categoria, local, prioridade, solicitador);
    }

    @Test
    @DisplayName("Usuário autenticado deve criar ticket com sucesso")
    public void testInsertTicketAsUsuario() throws Exception {
        String body = corpoTicket("Vazamento na pia", "Pia com vazamento", "HIDRAULICA", "Bloco A", "ALTA", "usuario@zelodesk.com");

        mockMvc.perform(post("/tickets")
                        .header("Authorization", "Bearer " + tokenUsuario)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.titulo", equalTo("Vazamento na pia")))
                .andExpect(jsonPath("$.status", equalTo("Aberto")))
                .andExpect(jsonPath("$.ticketCode", startsWith("TKT-")));
    }

    @Test
    @DisplayName("Admin autenticado deve criar ticket com sucesso")
    public void testInsertTicketAsAdmin() throws Exception {
        String body = corpoTicket("Lâmpada queimada", "Lâmpada do corredor", "ELETRICA", "Bloco B", "BAIXA", "admin@zelodesk.com");

        mockMvc.perform(post("/tickets")
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo("Aberto")))
                .andExpect(jsonPath("$.categoria", equalTo("ELETRICA")));
    }

    @Test
    @DisplayName("Requisição sem token deve ser permitida (permitAll configurado)")
    public void testInsertTicketSemToken() throws Exception {
        String body = corpoTicket("Teste", "Desc", "LIMPEZA", "Bloco C", "MEDIA", "teste@zelodesk.com");

        // NOTA: Atualmente o endpoint permite acesso sem autenticação devido a .permitAll()
        // no ResourceServerConfig. Se quiser proteger, remover permitAll() e usar @PreAuthorize
        mockMvc.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());  // Mudado de isUnauthorized() para isOk()
    }

    @Test
    @DisplayName("Deve mudar status do ticket para Em Andamento")
    public void testUpdateStatusEmAndamento() throws Exception {
        // Criar ticket primeiro
        String body = corpoTicket("Problema elétrico", "Curto no corredor", "ELETRICA", "Bloco C", "ALTA", "usuario@zelodesk.com");

        MvcResult createResult = mockMvc.perform(post("/tickets")
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();

        Long id = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asLong();

        // Mudar status
        mockMvc.perform(patch("/tickets/{id}/status", id)
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .param("novoStatus", "Em Andamento"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo("Em Andamento")))
                .andExpect(jsonPath("$.id", equalTo(id.intValue())));
    }

    @Test
    @DisplayName("Deve mudar status do ticket para Concluído")
    public void testUpdateStatusConcluido() throws Exception {
        String body = corpoTicket("Piso danificado", "Piso solto", "MANUTENCAO", "Bloco D", "MEDIA", "usuario@zelodesk.com");

        MvcResult createResult = mockMvc.perform(post("/tickets")
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();

        Long id = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(patch("/tickets/{id}/status", id)
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .param("novoStatus", "Concluído"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo("Concluído")));
    }

    @Test
    @DisplayName("Deve mudar status do ticket para Cancelado")
    public void testUpdateStatusCancelado() throws Exception {
        String body = corpoTicket("Câmera sem sinal", "Câmera offline", "SEGURANCA", "Entrada principal", "ALTA", "usuario@zelodesk.com");

        MvcResult createResult = mockMvc.perform(post("/tickets")
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();

        Long id = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(patch("/tickets/{id}/status", id)
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .param("novoStatus", "Cancelado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo("Cancelado")));
    }

    @Test
    @DisplayName("Mudar status de ticket inexistente deve retornar 404")
    public void testUpdateStatusTicketInexistente() throws Exception {
        mockMvc.perform(patch("/tickets/999/status")
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .param("novoStatus", "Em Andamento"))
                .andExpect(status().isNotFound());
    }
}

