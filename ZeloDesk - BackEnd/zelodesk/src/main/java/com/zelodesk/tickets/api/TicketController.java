package com.zelodesk.tickets.api;

import com.zelodesk.tickets.application.TicketService;
import com.zelodesk.tickets.domain.CategoriaEnum;
import com.zelodesk.tickets.dto.TicketComentarioDTO;
import com.zelodesk.tickets.dto.TicketConclusaoDTO;
import com.zelodesk.tickets.dto.TicketDTO;
import com.zelodesk.tickets.dto.TicketMetricaDTO;
import com.zelodesk.tickets.dto.TicketTriagemDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/tickets")
public class TicketController {

    private static final String QUALQUER_PERFIL = "hasAnyAuthority('ROLE_ADMIN','ROLE_USUARIO','ROLE_SOLICITANTE','ROLE_TRIAGEM','ROLE_EXECUTOR')";

    @Autowired
    private TicketService ticketService;

    @PreAuthorize(QUALQUER_PERFIL)
    @PostMapping
    public ResponseEntity<TicketDTO> insertTicket(@Valid @RequestBody TicketDTO ticketDTO,
                                                  @AuthenticationPrincipal Jwt jwt) {
        TicketDTO response = ticketService.insertTicket(ticketDTO, username(jwt));
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize(QUALQUER_PERFIL)
    @GetMapping
    public ResponseEntity<List<TicketDTO>> findAll(@RequestParam(required = false) String status,
                                                   @RequestParam(required = false) CategoriaEnum categoria,
                                                   @RequestParam(required = false) String localPredio) {
        return ResponseEntity.ok(ticketService.findAll(status, categoria, localPredio));
    }

    @PreAuthorize(QUALQUER_PERFIL)
    @PatchMapping(value = "/{id}/status")
    public ResponseEntity<TicketDTO> updateStatus(@PathVariable Long id, @RequestParam String novoStatus) {
        TicketDTO ticketDTO = ticketService.updateStatus(id, novoStatus);
        return ResponseEntity.ok().body(ticketDTO);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_TRIAGEM')")
    @PatchMapping(value = "/{id}/triagem")
    public ResponseEntity<TicketDTO> realizarTriagem(@PathVariable Long id,
                                                     @Valid @RequestBody TicketTriagemDTO triagemDTO,
                                                     @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(ticketService.realizarTriagem(id, triagemDTO, username(jwt)));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EXECUTOR')")
    @PatchMapping(value = "/{id}/assumir")
    public ResponseEntity<TicketDTO> assumirTicket(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(ticketService.assumirTicket(id, username(jwt)));
    }

    @PreAuthorize(QUALQUER_PERFIL)
    @PostMapping(value = "/{id}/comentarios")
    public ResponseEntity<TicketDTO> adicionarComentario(@PathVariable Long id,
                                                         @Valid @RequestBody TicketComentarioDTO comentarioDTO,
                                                         @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(ticketService.adicionarComentario(id, comentarioDTO, username(jwt)));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EXECUTOR')")
    @PatchMapping(value = "/{id}/concluir")
    public ResponseEntity<TicketDTO> concluirTicket(@PathVariable Long id,
                                                    @Valid @RequestBody TicketConclusaoDTO conclusaoDTO,
                                                    @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(ticketService.concluirTicket(id, conclusaoDTO, username(jwt)));
    }

    @PreAuthorize(QUALQUER_PERFIL)
    @GetMapping(value = "/metricas/categoria")
    public ResponseEntity<List<TicketMetricaDTO>> metricasPorCategoria() {
        List<TicketMetricaDTO> metricas = ticketService.metricaTicketsPorCategoria();
        return ResponseEntity.ok().body(metricas);
    }

    @PreAuthorize(QUALQUER_PERFIL)
    @GetMapping(value = "/{id}")
    public ResponseEntity<TicketDTO> findById(@PathVariable Long id) {
        TicketDTO ticketDTO = ticketService.findById(id);
        return ResponseEntity.ok().body(ticketDTO);
    }

    private String username(Jwt jwt) {
        if (jwt == null) {
            return null;
        }
        return jwt.getClaimAsString("username");
    }
}
