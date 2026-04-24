package com.zelodesk.services;

import com.zelodesk.dtos.TicketDTO;
import com.zelodesk.dtos.TicketMetricaDTO;
import com.zelodesk.entities.Ticket;
import com.zelodesk.enums.StatusEnum;
import com.zelodesk.repositories.TicketRepository;
import com.zelodesk.services.exceptions.ResourceNotFoundException;
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
        ticket.setStatus(StatusEnum.ABERTO);
        ticket.setSolicitador(ticketDTO.getSolicitador());
        ticket = ticketRepository.save(ticket);
        return new TicketDTO(ticket);
    }

    public TicketDTO updateStatus(Long id, StatusEnum novoStatus) {
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
