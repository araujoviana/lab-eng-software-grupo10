package com.zelodesk.tickets.dto;

import com.zelodesk.tickets.domain.TicketComentario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class TicketComentarioDTO {

    private Long id;
    private LocalDateTime criadoEm;
    private String autor;

    @NotBlank(message = "Comentario e obrigatorio")
    @Size(min = 3, message = "Comentario deve ter pelo menos 3 caracteres")
    private String texto;

    public TicketComentarioDTO() {
    }

    public TicketComentarioDTO(TicketComentario comentario) {
        id = comentario.getId();
        criadoEm = comentario.getCriadoEm();
        autor = comentario.getAutor();
        texto = comentario.getTexto();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
