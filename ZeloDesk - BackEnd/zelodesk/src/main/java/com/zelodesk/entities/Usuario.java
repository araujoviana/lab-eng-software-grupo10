package com.zelodesk.entities;

import com.zelodesk.enums.ClientEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "tb_Usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String email;

    private String senha;

    private ClientEnum role;

    @OneToMany(mappedBy = "solicitante")
    private List<Ticket> ticketsCriados;

    @OneToMany(mappedBy = "executor")
    private List<Ticket> ticketsExecutados;
}
