package com.zelodesk.tickets.repository;

import com.zelodesk.tickets.domain.Ticket;
import com.zelodesk.tickets.dto.TicketMetricaDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket,Long> {

    @Query("SELECT new com.zelodesk.tickets.dto.TicketMetricaDTO(t.categoria, COUNT(t)) FROM Ticket t GROUP BY t.categoria")
    List<TicketMetricaDTO> countTicketsByCategoria();

    @Query("""
            SELECT t FROM Ticket t
            WHERE (:status IS NULL OR t.status = :status)
              AND (:categoria IS NULL OR t.categoria = :categoria)
            ORDER BY t.atualizadoEm DESC, t.id DESC
            """)
    List<Ticket> search(
            @Param("status") String status,
            @Param("categoria") com.zelodesk.tickets.domain.CategoriaEnum categoria);

    @Query("""
            SELECT t FROM Ticket t
            WHERE (:status IS NULL OR t.status = :status)
              AND (:categoria IS NULL OR t.categoria = :categoria)
              AND LOWER(t.localPredio) LIKE LOWER(CONCAT('%', :localPredio, '%'))
            ORDER BY t.atualizadoEm DESC, t.id DESC
            """)
    List<Ticket> searchWithLocalPredio(
            @Param("status") String status,
            @Param("categoria") com.zelodesk.tickets.domain.CategoriaEnum categoria,
            @Param("localPredio") String localPredio);
}
