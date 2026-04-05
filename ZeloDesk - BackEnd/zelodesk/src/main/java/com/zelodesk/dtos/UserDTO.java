package com.zelodesk.dtos;

import com.zelodesk.entities.Role;
import com.zelodesk.entities.Usuario;

import java.util.HashSet;
import java.util.Set;

public class UserDTO {

    private Long id;
    private String nome;
    private String email;
    private Set<String> roles = new HashSet<>();

    public UserDTO() {
    }

    public UserDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        for (Role role : usuario.getRoles()) {
            this.roles.add(role.getAuthority());
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}


