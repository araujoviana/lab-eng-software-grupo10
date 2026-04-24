package com.zelodesk.tickets.repository;

import com.zelodesk.tickets.domain.Ticket;
import com.zelodesk.tickets.dto.TicketMetricaDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket,Long> {

    @Query("SELECT new com.zelodesk.tickets.dto.TicketMetricaDTO(t.categoria, COUNT(t)) FROM Ticket t GROUP BY t.categoria")
    List<TicketMetricaDTO> countTicketsByCategoria();
}
