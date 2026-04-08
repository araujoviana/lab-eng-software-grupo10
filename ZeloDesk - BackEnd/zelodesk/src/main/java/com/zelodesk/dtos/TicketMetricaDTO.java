package com.zelodesk.dtos;

import com.zelodesk.enums.CategoriaEnum;

public class TicketMetricaDTO {

    private CategoriaEnum categoria;
    private Long quantidade;

    public TicketMetricaDTO(CategoriaEnum categoria, Long quantidade) {
        this.categoria = categoria;
        this.quantidade = quantidade;
    }

    public CategoriaEnum getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaEnum categoria) {
        this.categoria = categoria;
    }

    public Long getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Long quantidade) {
        this.quantidade = quantidade;
    }
}
