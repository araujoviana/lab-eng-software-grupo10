package com.zelodesk.repositories;

import com.zelodesk.dtos.TicketMetricaDTO;
import com.zelodesk.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket,Long> {

    @Query("SELECT new com.zelodesk.dtos.TicketMetricaDTO(t.categoria, COUNT(t)) FROM Ticket t GROUP BY t.categoria")
    List<TicketMetricaDTO> countTicketsByCategoria();
}
