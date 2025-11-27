package com.finan.orcamento.repositories;

import com.finan.orcamento.model.UsuarioModel;
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
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {

    List<UsuarioModel> findByNomeUsuarioContainingIgnoreCase(String nome);

    @Query("SELECT u FROM UsuarioModel u " +
            "LEFT JOIN u.orcamentos o " +
            "WHERE (cast(:dataInicio as date) IS NULL OR u.dataCadastro >= :dataInicio) " +
            "AND (cast(:dataFim as date) IS NULL OR u.dataCadastro <= :dataFim) " +
            "AND (:nome IS NULL OR LOWER(u.nomeUsuario) LIKE LOWER(CONCAT('%', :nome, '%'))) " + // NOVO FILTRO
            "GROUP BY u " +
            "HAVING (cast(:valorMin as bigdecimal) IS NULL OR SUM(COALESCE(o.valorOrcamento, 0)) >= :valorMin) " +
            "AND (cast(:valorMax as bigdecimal) IS NULL OR SUM(COALESCE(o.valorOrcamento, 0)) <= :valorMax)")
    Page<UsuarioModel> findUsuariosReport(
            @Param("nome") String nome,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            @Param("valorMin") BigDecimal valorMin,
            @Param("valorMax") BigDecimal valorMax,
            Pageable pageable
    );
}