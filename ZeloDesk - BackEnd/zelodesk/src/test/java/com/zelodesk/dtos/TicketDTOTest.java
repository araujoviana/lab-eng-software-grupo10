package com.zelodesk.dtos;

import com.zelodesk.entities.Ticket;
import com.zelodesk.enums.CategoriaEnum;
import com.zelodesk.enums.PrioridadeEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TicketDTOTest {

    @Test
    void deveCriarDtoAPartirDeTicket() {
        Ticket ticket = new Ticket(
                7L,
                "Vazamento no corredor",
                "Existe um vazamento proximo ao elevador.",
                CategoriaEnum.HIDRAULICA,
                "Bloco A - 2o andar",
                PrioridadeEnum.ALTA,
                "ABERTO",
                "Carlos"
        );

        TicketDTO dto = new TicketDTO(ticket);

        assertEquals(7L, dto.getId());
        assertEquals("TKT-007", dto.getTicketCode());
        assertEquals("Vazamento no corredor", dto.getTitulo());
        assertEquals("Existe um vazamento proximo ao elevador.", dto.getDescricao());
        assertEquals(CategoriaEnum.HIDRAULICA, dto.getCategoria());
        assertEquals("Bloco A - 2o andar", dto.getLocalPredio());
        assertEquals(PrioridadeEnum.ALTA, dto.getPrioridade());
        assertEquals("ABERTO", dto.getStatus());
        assertEquals("Carlos", dto.getSolicitador());
    }
}
