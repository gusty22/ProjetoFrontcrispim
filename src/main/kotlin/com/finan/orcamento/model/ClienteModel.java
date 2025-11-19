package com.finan.orcamento.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp; // Importante

import java.io.Serializable;
import java.time.LocalDate; // Importante
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "cliente")
public class ClienteModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "nome_cliente")
    private String nome;

    @NotBlank
    @Column(name = "cpf", unique = true)
    private String cpf;

    // NOVO CAMPO: Data de Cadastro
    @CreationTimestamp
    @Column(name = "data_cadastro", updatable = false)
    private LocalDate dataCadastro;

    @JsonIgnore
    @OneToMany(mappedBy = "cliente")
    private List<OrcamentoModel> orcamentos = new ArrayList<>();

    public ClienteModel() {}

    public ClienteModel(Long id, String nome, String cpf) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public LocalDate getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDate dataCadastro) { this.dataCadastro = dataCadastro; }

    public List<OrcamentoModel> getOrcamentos() { return orcamentos; }
    public void setOrcamentos(List<OrcamentoModel> orcamentos) { this.orcamentos = orcamentos; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClienteModel that = (ClienteModel) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}