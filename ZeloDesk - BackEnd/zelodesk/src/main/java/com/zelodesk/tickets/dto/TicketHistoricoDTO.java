package com.zelodesk.tickets.dto;

import com.zelodesk.tickets.domain.TicketHistorico;

import java.time.LocalDateTime;

public class TicketHistoricoDTO {

    private Long id;
    private LocalDateTime criadoEm;
    private String autor;
    private String acao;
    private String descricao;

    public TicketHistoricoDTO() {
    }

    public TicketHistoricoDTO(TicketHistorico historico) {
        id = historico.getId();
        criadoEm = historico.getCriadoEm();
        autor = historico.getAutor();
        acao = historico.getAcao();
        descricao = historico.getDescricao();
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

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
