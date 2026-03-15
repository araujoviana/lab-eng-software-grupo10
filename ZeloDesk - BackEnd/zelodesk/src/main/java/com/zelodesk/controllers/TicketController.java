package com.zelodesk.controllers;

import com.zelodesk.dtos.TicketDTO;
import com.zelodesk.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketDTO> insertTicket(@RequestBody TicketDTO ticketDTO){
        ticketDTO = ticketService.insertTicket(ticketDTO);
        return ResponseEntity.ok().body(ticketDTO);
    }
}
