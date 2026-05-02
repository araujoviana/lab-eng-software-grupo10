package com.zelodesk.tickets.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TicketConclusaoDTO {

    @NotBlank(message = "Comentario de atendimento e obrigatorio")
    @Size(min = 5, message = "Comentario de atendimento deve ter pelo menos 5 caracteres")
    private String comentarioAtendimento;

    @NotBlank(message = "Evidencia final e obrigatoria")
    private String evidenciaUrl;

    public String getComentarioAtendimento() {
        return comentarioAtendimento;
    }

    public void setComentarioAtendimento(String comentarioAtendimento) {
        this.comentarioAtendimento = comentarioAtendimento;
    }

    public String getEvidenciaUrl() {
        return evidenciaUrl;
    }

    public void setEvidenciaUrl(String evidenciaUrl) {
        this.evidenciaUrl = evidenciaUrl;
    }
}
