package com.zelodesk.tickets.dto;

import com.zelodesk.tickets.domain.CategoriaEnum;
import com.zelodesk.tickets.domain.PrioridadeEnum;
import com.zelodesk.tickets.domain.Ticket;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TicketDTO {

    private Long id;
    private String ticketCode;

    @NotBlank(message = "Titulo e obrigatorio")
    private String titulo;

    @NotBlank(message = "Descricao e obrigatoria")
    @Size(min = 10, message = "Descricao deve ter pelo menos 10 caracteres")
    private String descricao;

    @NotNull(message = "Categoria e obrigatoria")
    private CategoriaEnum categoria;

    @NotBlank(message = "Localizacao e obrigatoria")
    private String localPredio;

    @NotNull(message = "Prioridade e obrigatoria")
    private PrioridadeEnum prioridade;

    private String status;
    private String solicitador;
    private Long solicitanteId;
    private Long executorId;
    private String executorNome;
    private String responsavelNome;
    private String anexoUrl;
    private String evidenciaUrl;
    private String comentarioConclusao;
    private String motivoEncerramento;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
    private LocalDateTime concluidoEm;
    private List<TicketHistoricoDTO> historico = new ArrayList<>();
    private List<TicketComentarioDTO> comentarios = new ArrayList<>();

    public TicketDTO() {
    }

    public TicketDTO(Long id, String ticketCode, String solicitador, String status, PrioridadeEnum prioridade,
                     String localPredio, CategoriaEnum categoria, String descricao, String titulo) {
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

    public TicketDTO(Ticket ticket) {
        id = ticket.getId();
        ticketCode = ticket.getId() == null ? null : String.format("TKT-%03d", ticket.getId());
        titulo = ticket.getTitulo();
        descricao = ticket.getDescricao();
        categoria = ticket.getCategoria();
        localPredio = ticket.getLocalPredio();
        prioridade = ticket.getPrioridade();
        status = ticket.getStatus();
        solicitador = ticket.getSolicitador();
        solicitanteId = ticket.getSolicitante() == null ? null : ticket.getSolicitante().getId();
        executorId = ticket.getExecutor() == null ? null : ticket.getExecutor().getId();
        executorNome = ticket.getExecutor() == null ? null : ticket.getExecutor().getNome();
        responsavelNome = ticket.getResponsavelNome();
        anexoUrl = ticket.getAnexoUrl();
        evidenciaUrl = ticket.getEvidenciaUrl();
        comentarioConclusao = ticket.getComentarioConclusao();
        motivoEncerramento = ticket.getMotivoEncerramento();
        criadoEm = ticket.getCriadoEm();
        atualizadoEm = ticket.getAtualizadoEm();
        concluidoEm = ticket.getConcluidoEm();
        historico = ticket.getHistorico().stream().map(TicketHistoricoDTO::new).toList();
        comentarios = ticket.getComentarios().stream().map(TicketComentarioDTO::new).toList();
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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

    public PrioridadeEnum getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(PrioridadeEnum prioridade) {
        this.prioridade = prioridade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSolicitador() {
        return solicitador;
    }

    public void setSolicitador(String solicitador) {
        this.solicitador = solicitador;
    }

    public Long getSolicitanteId() {
        return solicitanteId;
    }

    public void setSolicitanteId(Long solicitanteId) {
        this.solicitanteId = solicitanteId;
    }

    public Long getExecutorId() {
        return executorId;
    }

    public void setExecutorId(Long executorId) {
        this.executorId = executorId;
    }

    public String getExecutorNome() {
        return executorNome;
    }

    public void setExecutorNome(String executorNome) {
        this.executorNome = executorNome;
    }

    public String getResponsavelNome() {
        return responsavelNome;
    }

    public void setResponsavelNome(String responsavelNome) {
        this.responsavelNome = responsavelNome;
    }

    public String getAnexoUrl() {
        return anexoUrl;
    }

    public void setAnexoUrl(String anexoUrl) {
        this.anexoUrl = anexoUrl;
    }

    public String getEvidenciaUrl() {
        return evidenciaUrl;
    }

    public void setEvidenciaUrl(String evidenciaUrl) {
        this.evidenciaUrl = evidenciaUrl;
    }

    public String getComentarioConclusao() {
        return comentarioConclusao;
    }

    public void setComentarioConclusao(String comentarioConclusao) {
        this.comentarioConclusao = comentarioConclusao;
    }

    public String getMotivoEncerramento() {
        return motivoEncerramento;
    }

    public void setMotivoEncerramento(String motivoEncerramento) {
        this.motivoEncerramento = motivoEncerramento;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

    public LocalDateTime getConcluidoEm() {
        return concluidoEm;
    }

    public void setConcluidoEm(LocalDateTime concluidoEm) {
        this.concluidoEm = concluidoEm;
    }

    public List<TicketHistoricoDTO> getHistorico() {
        return historico;
    }

    public void setHistorico(List<TicketHistoricoDTO> historico) {
        this.historico = historico;
    }

    public List<TicketComentarioDTO> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<TicketComentarioDTO> comentarios) {
        this.comentarios = comentarios;
    }
}
