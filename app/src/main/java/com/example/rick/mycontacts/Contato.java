package com.example.rick.mycontacts;

import java.io.Serializable;

/**
 * Created by rick on 21/05/16.
 */
public class Contato implements Serializable {
    private String nome;
    private String email;
    private String telefone;
    private String foto;
    private long id;

    @Override
    public String toString() {
        return "(" + id + ")";
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getFoto() {
        return foto;
    }

    public Long getId() {
        return id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public void setId(long id) {
        this.id = id;
    }
}
