package com.zelodesk.dtos;

import com.zelodesk.enums.PrioridadeEnum;

public class TicketDTO {

    private Long id;
    private String titulo;
    private String descricao;
    private String categoria;  // Transformar em enum
    private String localPredio;
    private PrioridadeEnum prioridade; // Transformar em enum
    private String status;
    private String solicitador;

    public TicketDTO(){}

    public TicketDTO(Long id, String solicitador, String status, PrioridadeEnum prioridade, String localPredio, String categoria, String descricao, String titulo) {
        this.id = id;
        this.solicitador = solicitador;
        this.status = status;
        this.prioridade = prioridade;
        this.localPredio = localPredio;
        this.categoria = categoria;
        this.descricao = descricao;
        this.titulo = titulo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSolicitador() {
        return solicitador;
    }

    public void setSolicitador(String solicitador) {
        this.solicitador = solicitador;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
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
