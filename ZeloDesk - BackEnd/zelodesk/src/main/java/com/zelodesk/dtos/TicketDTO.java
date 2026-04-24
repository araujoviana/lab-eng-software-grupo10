package com.zelodesk.dtos;

import com.zelodesk.entities.Ticket;
import com.zelodesk.enums.CategoriaEnum;
import com.zelodesk.enums.PrioridadeEnum;
import com.zelodesk.enums.StatusEnum;

public class TicketDTO {

    private Long id;
    private String ticketCode;
    private String titulo;
    private String descricao;
    private CategoriaEnum categoria;
    private String localPredio;
    private PrioridadeEnum prioridade;
    private StatusEnum status;
    private String solicitador;

    public TicketDTO(){}

    public TicketDTO(Long id,String ticketCode, String solicitador, StatusEnum status, PrioridadeEnum prioridade, String localPredio, CategoriaEnum categoria, String descricao, String titulo) {
        this.id = id;
        this.ticketCode = ticketCode;
        this.solicitador = solicitador;
        this.status = status;
        this.prioridade = prioridade;
        this.localPredio = localPredio;
        this.categoria = categoria;
        this.descricao = descricao;
        this.titulo = titulo;
    }

    public TicketDTO(Ticket ticket){
        id = ticket.getId();
        ticketCode = String.format("TKT-%03d", ticket.getId());
        titulo = ticket.getTitulo();
        descricao = ticket.getDescricao();
        categoria = ticket.getCategoria();
        localPredio = ticket.getLocalPredio();
        prioridade = ticket.getPrioridade();
        status = ticket.getStatus();
        solicitador = ticket.getSolicitador();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public String getSolicitador() {
        return solicitador;
    }

    public void setSolicitador(String solicitador) {
        this.solicitador = solicitador;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public PrioridadeEnum getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(PrioridadeEnum prioridade) {
        this.prioridade = prioridade;
    }

    public String getLocalPredio() {
        return localPredio;
    }

    public void setLocalPredio(String localPredio) {
        this.localPredio = localPredio;
    }

    public CategoriaEnum getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaEnum categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
