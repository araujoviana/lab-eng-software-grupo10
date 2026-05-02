package com.zelodesk.tickets.application;

import com.zelodesk.shared.exception.ResourceNotFoundException;
import com.zelodesk.tickets.domain.CategoriaEnum;
import com.zelodesk.tickets.domain.PrioridadeEnum;
import com.zelodesk.tickets.dto.TicketConclusaoDTO;
import com.zelodesk.tickets.dto.TicketDTO;
import com.zelodesk.tickets.dto.TicketTriagemDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Testes do TicketService")
public class TicketServiceTests {

    @Autowired
    private TicketService ticketService;

    private TicketDTO criarTicketValido() {
        TicketDTO dto = new TicketDTO();
        dto.setTitulo("Vazamento na pia");
        dto.setDescricao("Pia do banheiro com vazamento constante");
        dto.setCategoria(CategoriaEnum.HIDRAULICA);
        dto.setLocalPredio("Bloco A - Sala 10");
        dto.setPrioridade(PrioridadeEnum.ALTA);
        dto.setSolicitador("Maria Oliveira");
        dto.setAnexoUrl("https://exemplo.com/foto-vazamento.jpg");
        return dto;
    }

    private TicketTriagemDTO triagemParaExecutor() {
        TicketTriagemDTO dto = new TicketTriagemDTO();
        dto.setPrioridade(PrioridadeEnum.ALTA);
        dto.setDecisao("ATRIBUIR");
        dto.setExecutorId(7L);
        return dto;
    }

    @Test
    @DisplayName("UC-01 deve abrir ticket com status inicial e historico")
    public void deveAbrirTicketComHistoricoInicial() {
        TicketDTO resultado = ticketService.insertTicket(criarTicketValido(), "solicitante@zelodesk.com");

        assertNotNull(resultado.getId());
        assertTrue(resultado.getTicketCode().startsWith("TKT-"));
        assertEquals(TicketService.STATUS_ABERTO, resultado.getStatus());
        assertEquals("Maria Oliveira", resultado.getSolicitador());
        assertEquals(CategoriaEnum.HIDRAULICA, resultado.getCategoria());
        assertFalse(resultado.getHistorico().isEmpty());
        assertEquals("Ticket aberto", resultado.getHistorico().get(0).getAcao());
    }

    @Test
    @DisplayName("RF07 deve listar tickets cadastrados com filtros opcionais")
    public void deveListarTicketsComFiltroDeStatus() {
        TicketDTO criado = ticketService.insertTicket(criarTicketValido(), "solicitante@zelodesk.com");

        List<TicketDTO> abertos = ticketService.findAll(TicketService.STATUS_ABERTO, null, null);

        assertTrue(abertos.stream().anyMatch(ticket -> ticket.getId().equals(criado.getId())));
    }

    @Test
    @DisplayName("UC-02 deve realizar triagem e atribuir executor")
    public void deveRealizarTriagemEAtribuirExecutor() {
        TicketDTO criado = ticketService.insertTicket(criarTicketValido(), "solicitante@zelodesk.com");

        TicketDTO triado = ticketService.realizarTriagem(criado.getId(), triagemParaExecutor(), "triagem@zelodesk.com");

        assertEquals(TicketService.STATUS_EM_EXECUCAO, triado.getStatus());
        assertEquals(7L, triado.getExecutorId());
        assertEquals("Joao Pereira", triado.getResponsavelNome());
        assertTrue(triado.getHistorico().stream().anyMatch(item -> item.getAcao().equals("Triagem registrada")));
    }

    @Test
    @DisplayName("UC-02 deve exigir responsavel para atribuir ticket")
    public void deveExigirResponsavelNaTriagem() {
        TicketDTO criado = ticketService.insertTicket(criarTicketValido(), "solicitante@zelodesk.com");
        TicketTriagemDTO triagem = triagemParaExecutor();
        triagem.setExecutorId(null);

        assertThrows(IllegalArgumentException.class,
                () -> ticketService.realizarTriagem(criado.getId(), triagem, "triagem@zelodesk.com"));
    }

    @Test
    @DisplayName("UC-03 deve concluir ticket com comentario e evidencia")
    public void deveConcluirTicketComComentarioEEvidencia() {
        TicketDTO criado = ticketService.insertTicket(criarTicketValido(), "solicitante@zelodesk.com");
        TicketDTO triado = ticketService.realizarTriagem(criado.getId(), triagemParaExecutor(), "triagem@zelodesk.com");

        TicketConclusaoDTO conclusao = new TicketConclusaoDTO();
        conclusao.setComentarioAtendimento("Reparo realizado e ambiente conferido.");
        conclusao.setEvidenciaUrl("https://exemplo.com/evidencia-final.jpg");

        TicketDTO concluido = ticketService.concluirTicket(triado.getId(), conclusao, "executor@zelodesk.com");

        assertEquals(TicketService.STATUS_CONCLUIDO, concluido.getStatus());
        assertEquals("https://exemplo.com/evidencia-final.jpg", concluido.getEvidenciaUrl());
        assertFalse(concluido.getComentarios().isEmpty());
        assertTrue(concluido.getHistorico().stream().anyMatch(item -> item.getAcao().equals("Ticket concluido")));
    }

    @Test
    @DisplayName("UC-03 deve bloquear conclusao sem evidencia")
    public void deveBloquearConclusaoSemEvidencia() {
        TicketDTO criado = ticketService.insertTicket(criarTicketValido(), "solicitante@zelodesk.com");
        TicketDTO triado = ticketService.realizarTriagem(criado.getId(), triagemParaExecutor(), "triagem@zelodesk.com");

        TicketConclusaoDTO conclusao = new TicketConclusaoDTO();
        conclusao.setComentarioAtendimento("Reparo realizado.");
        conclusao.setEvidenciaUrl(" ");

        assertThrows(IllegalArgumentException.class,
                () -> ticketService.concluirTicket(triado.getId(), conclusao, "executor@zelodesk.com"));
    }

    @Test
    @DisplayName("Deve lancar excecao ao buscar ticket inexistente")
    public void deveFalharParaTicketInexistente() {
        assertThrows(ResourceNotFoundException.class, () -> ticketService.findById(999L));
    }
}
