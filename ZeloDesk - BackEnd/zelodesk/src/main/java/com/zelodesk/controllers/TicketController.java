package com.zelodesk.controllers;

import com.zelodesk.dtos.TicketDTO;
import com.zelodesk.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping(value = "/{id}/status")
    public ResponseEntity<TicketDTO> updateStatus(@PathVariable Long id, @RequestParam String novoStatus){
        TicketDTO ticketDTO = ticketService.updateStatus(id, novoStatus);
        return ResponseEntity.ok().body(ticketDTO);
    }
}
