package com.zelodesk.entities;

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
    private String categoria;
    private String localizacao;
    private String prioridade;
    private String status;

    @ManyToOne
    private Usuario solicitante;

    @ManyToOne
    private Usuario executor;
}
