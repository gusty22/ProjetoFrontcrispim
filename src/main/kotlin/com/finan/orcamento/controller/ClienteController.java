package com.finan.orcamento.controller;

import com.finan.orcamento.model.ClienteModel;
import com.finan.orcamento.repositories.ClienteRepository;
import com.finan.orcamento.service.ClienteService;
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
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping("/clientes")
    public String paginaClientes() { return "clientePage"; }

    // --- API REST ---

    @GetMapping("/api/clientes")
    @ResponseBody
    public ResponseEntity<List<ClienteModel>> listarTodosClientes() {
        return ResponseEntity.ok(clienteService.buscarTodos());
    }

    @GetMapping("/api/clientes/{id}")
    @ResponseBody
    public ResponseEntity<ClienteModel> obterClientePorId(@PathVariable Long id) {
        try { return ResponseEntity.ok(clienteService.buscarPorId(id)); }
        catch (RuntimeException e) { return ResponseEntity.notFound().build(); }
    }

    @PostMapping("/api/clientes")
    @ResponseBody
    public ResponseEntity<ClienteModel> salvarOuAtualizarCliente(@RequestBody ClienteModel clienteModel) {
        return ResponseEntity.ok(clienteService.salvarOuAtualizar(clienteModel));
    }

    @DeleteMapping("/api/clientes/{id}")
    @ResponseBody
    public ResponseEntity<Void> deletarCliente(@PathVariable Long id) {
        try { clienteService.deletar(id); return ResponseEntity.noContent().build(); }
        catch (Exception e) { return ResponseEntity.notFound().build(); }
    }

    @GetMapping("/api/clientes/pesquisa")
    @ResponseBody
    public ResponseEntity<List<ClienteModel>> pesquisarClientes(@RequestParam("termo") String termo) {
        return ResponseEntity.ok(clienteService.pesquisarPorNomeOuCpf(termo));
    }

    // RELATÓRIO PAGINADO (Nome + Data + Valor Gasto)
    @GetMapping("/api/clientes/relatorio")
    @ResponseBody
    public ResponseEntity<Page<ClienteModel>> relatorioClientes(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "nome", required = false) String nome, // NOVO PARÂMETRO
            @RequestParam(value = "dataInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(value = "dataFim", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(value = "valorMin", required = false) BigDecimal valorMin,
            @RequestParam(value = "valorMax", required = false) BigDecimal valorMax
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("dataCadastro").descending());
        Page<ClienteModel> resultado = clienteRepository.findClientesReport(nome, dataInicio, dataFim, valorMin, valorMax, pageable);
        return ResponseEntity.ok(resultado);
    }
}