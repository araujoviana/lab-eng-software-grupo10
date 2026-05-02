package com.zelodesk.tickets.dto;

import com.zelodesk.tickets.domain.CategoriaEnum;
import com.zelodesk.tickets.domain.PrioridadeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TicketTriagemDTO {

    @NotNull(message = "Prioridade e obrigatoria")
    private PrioridadeEnum prioridade;
    private CategoriaEnum categoria;
    private String localPredio;
    private Long executorId;
    private String responsavelNome;

    @NotBlank(message = "Decisao de triagem e obrigatoria")
    private String decisao;
    private String motivo;

    public PrioridadeEnum getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(PrioridadeEnum prioridade) {
        this.prioridade = prioridade;
    }

    public CategoriaEnum getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaEnum categoria) {
        this.categoria = categoria;
    }

    public String getLocalPredio() {
        return localPredio;
    }

    public void setLocalPredio(String localPredio) {
        this.localPredio = localPredio;
    }

    public Long getExecutorId() {
        return executorId;
    }

    public void setExecutorId(Long executorId) {
        this.executorId = executorId;
    }

    public String getResponsavelNome() {
        return responsavelNome;
    }

    public void setResponsavelNome(String responsavelNome) {
        this.responsavelNome = responsavelNome;
    }

    public String getDecisao() {
        return decisao;
    }

    public void setDecisao(String decisao) {
        this.decisao = decisao;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
