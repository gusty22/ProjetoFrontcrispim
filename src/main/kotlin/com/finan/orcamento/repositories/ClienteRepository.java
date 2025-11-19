package com.finan.orcamento.repositories;

import com.finan.orcamento.model.ClienteModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteModel, Long> {

    // Pesquisa por nome OU cpf (existente)
    List<ClienteModel> findByNomeContainingIgnoreCaseOrCpfContaining(String nome, String cpf);

    // NOVO: Busca com filtro de data e paginação
    Page<ClienteModel> findByDataCadastroBetween(LocalDate dataInicio, LocalDate dataFim, Pageable pageable);

    // NOVO: Busca geral com paginação (quando não há filtro de data)
    Page<ClienteModel> findAll(Pageable pageable);
}