package com.zelodesk.tickets.domain;

import com.zelodesk.identity.domain.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descricao;

    @Enumerated(EnumType.STRING)
    private CategoriaEnum categoria;  // Transformar em enum

    private String localPredio;

    @Enumerated(EnumType.STRING)
    private PrioridadeEnum prioridade; // Transformar em enum

    private String status;
    private String solicitador;
    private String responsavelNome;
    private String anexoUrl;
    private String evidenciaUrl;
    private String comentarioConclusao;
    private String motivoEncerramento;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
    private LocalDateTime concluidoEm;

    @ManyToOne
    private Usuario solicitante; // Relação com o usuario que solicita em caso de 

    @ManyToOne
    private Usuario executor;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("criadoEm ASC")
    private List<TicketHistorico> historico = new ArrayList<>();

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("criadoEm ASC")
    private List<TicketComentario> comentarios = new ArrayList<>();

    public Ticket(Long id, String titulo, String descricao, CategoriaEnum categoria, String localPredio, PrioridadeEnum prioridade, String status, String solicitador) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.categoria = categoria;
        this.localPredio = localPredio;
        this.prioridade = prioridade;
        this.status = status;
        this.solicitador = solicitador;
    }

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        criadoEm = now;
        atualizadoEm = now;
    }

    @PreUpdate
    public void preUpdate() {
        atualizadoEm = LocalDateTime.now();
    }

    public void addHistorico(String autor, String acao, String descricao) {
        TicketHistorico item = new TicketHistorico(null, this, LocalDateTime.now(), autor, acao, descricao);
        historico.add(item);
    }

    public void addComentario(String autor, String texto) {
        TicketComentario comentario = new TicketComentario(null, this, LocalDateTime.now(), autor, texto);
        comentarios.add(comentario);
    }
}
