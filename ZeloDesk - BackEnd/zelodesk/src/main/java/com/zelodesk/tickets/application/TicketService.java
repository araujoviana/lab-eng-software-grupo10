package com.zelodesk.tickets.application;

import com.zelodesk.shared.exception.ResourceNotFoundException;
import com.zelodesk.tickets.domain.Ticket;
import com.zelodesk.tickets.dto.TicketDTO;
import com.zelodesk.tickets.dto.TicketMetricaDTO;
import com.zelodesk.tickets.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    public TicketDTO insertTicket(TicketDTO ticketDTO){
        Ticket ticket = new Ticket();
        ticket.setTitulo(ticketDTO.getTitulo());
        ticket.setDescricao(ticketDTO.getDescricao());
        ticket.setCategoria(ticketDTO.getCategoria());
        ticket.setLocalPredio(ticketDTO.getLocalPredio());
        ticket.setPrioridade(ticketDTO.getPrioridade());
        ticket.setStatus("Aberto");
        ticket.setSolicitador(ticketDTO.getSolicitador());
        ticket = ticketRepository.save(ticket);
        return new TicketDTO(ticket);
    }

    public TicketDTO updateStatus(Long id, String novoStatus) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket não encontrado: " + id));
        ticket.setStatus(novoStatus);
        ticket = ticketRepository.save(ticket);
        return new TicketDTO(ticket);
    }

    public List<TicketMetricaDTO> metricaTicketsPorCategoria() {
        return ticketRepository.countTicketsByCategoria();
    }

    public TicketDTO findById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket não encontrado: " + id));
        return new TicketDTO(ticket);
    }
}
