package com.finan.orcamento.repositories;

import com.finan.orcamento.model.OrcamentoModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface OrcamentoRepository extends JpaRepository<OrcamentoModel, Long> {

    // Relatório Avançado de Orçamentos
    // Filtra por data de criação E valor individual do orçamento
    @Query("SELECT o FROM OrcamentoModel o " +
            "WHERE (cast(:dataInicio as date) IS NULL OR o.dataCriacao >= :dataInicio) " +
            "AND (cast(:dataFim as date) IS NULL OR o.dataCriacao <= :dataFim) " +
            "AND (cast(:valorMin as bigdecimal) IS NULL OR o.valorOrcamento >= :valorMin) " +
            "AND (cast(:valorMax as bigdecimal) IS NULL OR o.valorOrcamento <= :valorMax)")
    Page<OrcamentoModel> findOrcamentosReport(
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            @Param("valorMin") BigDecimal valorMin,
            @Param("valorMax") BigDecimal valorMax,
            Pageable pageable
    );
}