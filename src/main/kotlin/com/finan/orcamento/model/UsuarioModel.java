package com.finan.orcamento.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "usuario")
public class UsuarioModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "nome_usuario")
    private String nomeUsuario;

    @NotBlank
    @Column(name = "rg")
    private String rg;

    @NotBlank
    @Column(name = "cpf", unique = true)
    private String cpf;

    @NotBlank
    @Column(name = "nome_mae")
    private String nomeMae;

    @CreationTimestamp
    @Column(name = "data_cadastro", updatable = false)
    private LocalDate dataCadastro;

    @JsonIgnore
    @OneToMany(mappedBy = "usuario")
    private List<OrcamentoModel> orcamentos = new ArrayList<>();

    // Campo calculado virtual para o JSON (Total Vendido)
    @JsonProperty("totalVendido")
    public BigDecimal getTotalVendido() {
        if (orcamentos == null || orcamentos.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return orcamentos.stream()
                .map(OrcamentoModel::getValorOrcamento)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public UsuarioModel() {}

    public UsuarioModel(Long id, String nomeUsuario, String rg, String cpf, String nomeMae) {
        this.id = id;
        this.nomeUsuario = nomeUsuario;
        this.rg = rg;
        this.cpf = cpf;
        this.nomeMae = nomeMae;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNomeUsuario() { return nomeUsuario; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }
    public String getRg() { return rg; }
    public void setRg(String rg) { this.rg = rg; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getNomeMae() { return nomeMae; }
    public void setNomeMae(String nomeMae) { this.nomeMae = nomeMae; }
    public LocalDate getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDate dataCadastro) { this.dataCadastro = dataCadastro; }
    public List<OrcamentoModel> getOrcamentos() { return orcamentos; }
    public void setOrcamentos(List<OrcamentoModel> orcamentos) { this.orcamentos = orcamentos; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsuarioModel that = (UsuarioModel) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}