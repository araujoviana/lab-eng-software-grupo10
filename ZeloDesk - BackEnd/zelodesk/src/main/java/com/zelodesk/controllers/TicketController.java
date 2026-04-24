package com.zelodesk.controllers;

import com.zelodesk.dtos.TicketDTO;
import com.zelodesk.dtos.TicketMetricaDTO;
import com.zelodesk.enums.StatusEnum;
import com.zelodesk.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<TicketDTO> updateStatus(@PathVariable Long id, @RequestParam StatusEnum novoStatus){
        TicketDTO ticketDTO = ticketService.updateStatus(id, novoStatus);
        return ResponseEntity.ok().body(ticketDTO);
    }

    @GetMapping(value = "/metricas/categoria")
    public ResponseEntity<List<TicketMetricaDTO>> metricasPorCategoria() {
        List<TicketMetricaDTO> metricas = ticketService.metricaTicketsPorCategoria();
        return ResponseEntity.ok().body(metricas);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TicketDTO> findById(@PathVariable Long id) {
        TicketDTO ticketDTO = ticketService.findById(id);
        return ResponseEntity.ok().body(ticketDTO);
    }
}
