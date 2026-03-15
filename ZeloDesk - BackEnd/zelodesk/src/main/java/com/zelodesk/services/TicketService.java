package com.zelodesk.services;

import com.zelodesk.dtos.TicketDTO;
import com.zelodesk.entities.Ticket;
import com.zelodesk.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
