package com.zelodesk.tickets.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zelodesk.tickets.application.TicketService;
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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Testes do TicketController")
public class TicketControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String tokenSolicitante;
    private String tokenTriagem;
    private String tokenExecutor;

    @BeforeEach
    public void setup() throws Exception {
        tokenSolicitante = obterToken("solicitante@zelodesk.com", "123456");
        tokenTriagem = obterToken("triagem@zelodesk.com", "123456");
        tokenExecutor = obterToken("executor@zelodesk.com", "123456");
    }

    private String obterToken(String email, String senha) throws Exception {
        String body = "grant_type=password&username=" + email + "&password=" + senha
                + "&scope=read%20write";

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

    private String corpoTicket() {
        return """
                {
                  "titulo": "Vazamento na pia",
                  "descricao": "Pia do banheiro com vazamento constante",
                  "categoria": "HIDRAULICA",
                  "localPredio": "Bloco A",
                  "prioridade": "ALTA",
                  "solicitador": "Maria Oliveira",
                  "anexoUrl": "https://exemplo.com/foto-vazamento.jpg"
                }
                """;
    }

    private Long criarTicket() throws Exception {
        MvcResult createResult = mockMvc.perform(post("/tickets")
                        .header("Authorization", "Bearer " + tokenSolicitante)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpoTicket()))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asLong();
    }

    private Long criarTicketTriado() throws Exception {
        Long id = criarTicket();

        mockMvc.perform(patch("/tickets/{id}/triagem", id)
                        .header("Authorization", "Bearer " + tokenTriagem)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "prioridade": "ALTA",
                                  "decisao": "ATRIBUIR",
                                  "executorId": 7
                                }
                                """))
                .andExpect(status().isOk());

        return id;
    }

    @Test
    @DisplayName("UC-01 solicitante autenticado deve criar ticket")
    public void deveCriarTicketComoSolicitante() throws Exception {
        mockMvc.perform(post("/tickets")
                        .header("Authorization", "Bearer " + tokenSolicitante)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpoTicket()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.ticketCode", startsWith("TKT-")))
                .andExpect(jsonPath("$.status", equalTo(TicketService.STATUS_ABERTO)))
                .andExpect(jsonPath("$.historico[0].acao", equalTo("Ticket aberto")));
    }

    @Test
    @DisplayName("Endpoints de ticket devem exigir autenticacao")
    public void deveExigirAutenticacao() throws Exception {
        mockMvc.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpoTicket()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("RF07 deve listar tickets para usuario autenticado")
    public void deveListarTickets() throws Exception {
        criarTicket();

        mockMvc.perform(get("/tickets")
                        .header("Authorization", "Bearer " + tokenSolicitante))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", notNullValue()));
    }

    @Test
    @DisplayName("UC-02 triagem deve atribuir executor")
    public void deveRealizarTriagem() throws Exception {
        Long id = criarTicket();

        mockMvc.perform(patch("/tickets/{id}/triagem", id)
                        .header("Authorization", "Bearer " + tokenTriagem)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "prioridade": "ALTA",
                                  "decisao": "ATRIBUIR",
                                  "executorId": 7
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo(TicketService.STATUS_EM_EXECUCAO)))
                .andExpect(jsonPath("$.executorId", equalTo(7)))
                .andExpect(jsonPath("$.responsavelNome", equalTo("Joao Pereira")));
    }

    @Test
    @DisplayName("Solicitante nao deve realizar triagem")
    public void deveBloquearTriagemParaSolicitante() throws Exception {
        Long id = criarTicket();

        mockMvc.perform(patch("/tickets/{id}/triagem", id)
                        .header("Authorization", "Bearer " + tokenSolicitante)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "prioridade": "ALTA",
                                  "decisao": "ATRIBUIR",
                                  "executorId": 7
                                }
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("UC-03 executor deve concluir ticket com evidencia")
    public void deveConcluirTicket() throws Exception {
        Long id = criarTicketTriado();

        mockMvc.perform(patch("/tickets/{id}/concluir", id)
                        .header("Authorization", "Bearer " + tokenExecutor)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "comentarioAtendimento": "Reparo realizado e validado no local.",
                                  "evidenciaUrl": "https://exemplo.com/evidencia-final.jpg"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo(TicketService.STATUS_CONCLUIDO)))
                .andExpect(jsonPath("$.evidenciaUrl", equalTo("https://exemplo.com/evidencia-final.jpg")))
                .andExpect(jsonPath("$.comentarios[0].texto", equalTo("Reparo realizado e validado no local.")));
    }

    @Test
    @DisplayName("UC-03 conclusao sem evidencia deve retornar 400")
    public void deveBloquearConclusaoSemEvidencia() throws Exception {
        Long id = criarTicketTriado();

        mockMvc.perform(patch("/tickets/{id}/concluir", id)
                        .header("Authorization", "Bearer " + tokenExecutor)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "comentarioAtendimento": "Reparo realizado.",
                                  "evidenciaUrl": ""
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Ticket inexistente deve retornar 404")
    public void deveRetornar404ParaTicketInexistente() throws Exception {
        mockMvc.perform(get("/tickets/999")
                        .header("Authorization", "Bearer " + tokenSolicitante))
                .andExpect(status().isNotFound());
    }
}
