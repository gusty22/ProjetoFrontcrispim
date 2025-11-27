package com.finan.orcamento.repositories;

import com.finan.orcamento.model.ClienteModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteModel, Long> {

    List<ClienteModel> findByNomeContainingIgnoreCaseOrCpfContaining(String nome, String cpf);

    // Relatório Avançado de Clientes
    // Filtra por:
    // 1. Nome (parcial)
    // 2. Data de cadastro (intervalo)
    // 3. Soma dos orçamentos (Total Gasto - intervalo)
    @Query("SELECT c FROM ClienteModel c " +
            "LEFT JOIN c.orcamentos o " +
            "WHERE (cast(:dataInicio as date) IS NULL OR c.dataCadastro >= :dataInicio) " +
            "AND (cast(:dataFim as date) IS NULL OR c.dataCadastro <= :dataFim) " +
            "AND (:nome IS NULL OR LOWER(c.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) " + // Filtro de Nome
            "GROUP BY c " +
            "HAVING (cast(:valorMin as bigdecimal) IS NULL OR SUM(COALESCE(o.valorOrcamento, 0)) >= :valorMin) " +
            "AND (cast(:valorMax as bigdecimal) IS NULL OR SUM(COALESCE(o.valorOrcamento, 0)) <= :valorMax)")
    Page<ClienteModel> findClientesReport(
            @Param("nome") String nome,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            @Param("valorMin") BigDecimal valorMin,
            @Param("valorMax") BigDecimal valorMax,
            Pageable pageable
    );
}