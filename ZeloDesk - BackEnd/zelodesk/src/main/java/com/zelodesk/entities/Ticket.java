package com.zelodesk.entities;

import com.zelodesk.enums.PrioridadeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String categoria;  // Transformar em enum
    private String localPredio;
    @Enumerated(EnumType.STRING)
    private PrioridadeEnum prioridade; // Transformar em enum
    private String status;
    private String solicitador;

    @ManyToOne
    private Usuario solicitante; // Relação com o usuario que solicita em caso de 

    @ManyToOne
    private Usuario executor;

    public Ticket(Long id, String titulo, String descricao, String categoria, String localPredio, PrioridadeEnum prioridade, String status, String solicitador) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.categoria = categoria;
        this.localPredio = localPredio;
        this.prioridade = prioridade;
        this.status = status;
        this.solicitador = solicitador;
    }
}
