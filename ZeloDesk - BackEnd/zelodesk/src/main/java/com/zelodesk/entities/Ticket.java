package com.zelodesk.entities;

import com.zelodesk.enums.PrioridadeEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tb_tikcet")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descricao;
    private String categoria;  // Transformar em enum
    private String localPredio;
    private PrioridadeEnum prioridade; // Transformar em enum
    private String status;
    private String solicitador;

    @ManyToOne
    private Usuario solicitante; // Relação com o usuario que solicita em caso de 

    @ManyToOne
    private Usuario executor;
}
