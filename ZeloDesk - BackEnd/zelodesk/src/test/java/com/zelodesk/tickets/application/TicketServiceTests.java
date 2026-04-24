package com.zelodesk.tickets.application;

import com.zelodesk.shared.exception.ResourceNotFoundException;
import com.zelodesk.tickets.domain.CategoriaEnum;
import com.zelodesk.tickets.domain.PrioridadeEnum;
import com.zelodesk.tickets.dto.TicketDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Testes do TicketService")
public class TicketServiceTests {

    @Autowired
    private TicketService ticketService;

    private TicketDTO criarTicketValido() {
        TicketDTO dto = new TicketDTO();
        dto.setTitulo("Vazamento na pia");
        dto.setDescricao("Pia do banheiro com vazamento");
        dto.setCategoria(CategoriaEnum.HIDRAULICA);
        dto.setLocalPredio("Bloco A - Sala 10");
        dto.setPrioridade(PrioridadeEnum.ALTA);
        dto.setSolicitador("Usuario Teste");
        return dto;
    }

    @Test
    @DisplayName("Deve criar ticket com sucesso e status Aberto")
    public void testInsertTicket() {
        TicketDTO dto = criarTicketValido();

        TicketDTO resultado = ticketService.insertTicket(dto);

        assertNotNull(resultado, "TicketDTO não deve ser nulo");
        assertNotNull(resultado.getId(), "ID deve ser gerado");
        assertEquals("Vazamento na pia", resultado.getTitulo());
        assertEquals("Aberto", resultado.getStatus(), "Status inicial deve ser Aberto");
        assertEquals(CategoriaEnum.HIDRAULICA, resultado.getCategoria());
        assertEquals(PrioridadeEnum.ALTA, resultado.getPrioridade());
        assertEquals("Usuario Teste", resultado.getSolicitador());
    }

    @Test
    @DisplayName("Deve gerar ticketCode no formato TKT-XXX ao criar")
    public void testInsertTicketGeraTicketCode() {
        TicketDTO dto = criarTicketValido();

        TicketDTO resultado = ticketService.insertTicket(dto);

        assertNotNull(resultado.getTicketCode(), "TicketCode não deve ser nulo");
        assertTrue(resultado.getTicketCode().startsWith("TKT-"), "TicketCode deve começar com TKT-");
    }

    @Test
    @DisplayName("Deve criar ticket com prioridade BAIXA")
    public void testInsertTicketPrioridadeBaixa() {
        TicketDTO dto = criarTicketValido();
        dto.setPrioridade(PrioridadeEnum.BAIXA);
        dto.setTitulo("Lâmpada queimada");
        dto.setCategoria(CategoriaEnum.ELETRICA);

        TicketDTO resultado = ticketService.insertTicket(dto);

        assertEquals(PrioridadeEnum.BAIXA, resultado.getPrioridade());
        assertEquals("Aberto", resultado.getStatus());
    }

    @Test
    @DisplayName("Deve mudar status do ticket para Em Andamento")
    public void testUpdateStatusEmAndamento() {
        TicketDTO criado = ticketService.insertTicket(criarTicketValido());

        TicketDTO atualizado = ticketService.updateStatus(criado.getId(), "Em Andamento");

        assertEquals("Em Andamento", atualizado.getStatus(), "Status deve ser Em Andamento");
        assertEquals(criado.getId(), atualizado.getId(), "ID deve ser o mesmo");
    }

    @Test
    @DisplayName("Deve mudar status do ticket para Concluído")
    public void testUpdateStatusConcluido() {
        TicketDTO criado = ticketService.insertTicket(criarTicketValido());

        TicketDTO atualizado = ticketService.updateStatus(criado.getId(), "Concluído");

        assertEquals("Concluído", atualizado.getStatus(), "Status deve ser Concluído");
    }

    @Test
    @DisplayName("Deve mudar status do ticket para Cancelado")
    public void testUpdateStatusCancelado() {
        TicketDTO criado = ticketService.insertTicket(criarTicketValido());

        TicketDTO atualizado = ticketService.updateStatus(criado.getId(), "Cancelado");

        assertEquals("Cancelado", atualizado.getStatus(), "Status deve ser Cancelado");
    }

    @Test
    @DisplayName("Deve lançar exceção ao mudar status de ticket inexistente")
    public void testUpdateStatusTicketInexistente() {
        assertThrows(ResourceNotFoundException.class, () -> {
            ticketService.updateStatus(999L, "Em Andamento");
        }, "Deve lançar ResourceNotFoundException para ticket inexistente");
    }
}
