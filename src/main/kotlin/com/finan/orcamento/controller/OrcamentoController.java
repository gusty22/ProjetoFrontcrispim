package com.finan.orcamento.controller;

import com.finan.orcamento.model.OrcamentoModel;
import com.finan.orcamento.repositories.OrcamentoRepository;
import com.finan.orcamento.service.OrcamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/orcamentos")
public class OrcamentoController {

    @Autowired
    private OrcamentoService orcamentoService;

    @Autowired
    private OrcamentoRepository orcamentoRepository;

    @GetMapping
    public String paginaOrcamentos() { return "orcamentoPage"; }

    // --- API REST ---

    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<List<OrcamentoModel>> buscaTodos(){
        return ResponseEntity.ok(orcamentoService.buscarCadastro());
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<OrcamentoModel> buscaPorId(@PathVariable Long id){
        try { return ResponseEntity.ok(orcamentoService.buscaId(id)); }
        catch (Exception e) { return ResponseEntity.notFound().build(); }
    }

    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<OrcamentoModel> salvar(@RequestBody OrcamentoModel model){
        try { return ResponseEntity.ok(orcamentoService.salvarOuAtualizar(model)); }
        catch (Exception e) { return ResponseEntity.badRequest().build(); }
    }

    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        try { orcamentoService.deletaOrcamento(id); return ResponseEntity.noContent().build(); }
        catch (Exception e) { return ResponseEntity.notFound().build(); }
    }

    // RELATÓRIO PAGINADO (Filtro de Data e Valor do Orçamento)
    @GetMapping("/api/relatorio")
    @ResponseBody
    public ResponseEntity<Page<OrcamentoModel>> relatorioOrcamentos(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "dataInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(value = "dataFim", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(value = "valorMin", required = false) BigDecimal valorMin,
            @RequestParam(value = "valorMax", required = false) BigDecimal valorMax
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("dataCriacao").descending());
        Page<OrcamentoModel> result = orcamentoRepository.findOrcamentosReport(dataInicio, dataFim, valorMin, valorMax, pageable);
        return ResponseEntity.ok(result);
    }
}