package com.finan.orcamento.controller;

import com.finan.orcamento.model.ClienteModel;
import com.finan.orcamento.repositories.ClienteRepository; // Acesso direto para relatório
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

import java.time.LocalDate;
import java.util.List;

@Controller
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteRepository clienteRepository; // Injetado para o relatório paginado

    // 1. Endpoint para servir a PÁGINA HTML
    @GetMapping("/clientes")
    public String paginaClientes() {
        return "clientePage";
    }

    // --- API REST PARA CLIENTES ---

    // 2. API para LISTAR (Lista simples para a tabela principal)
    @GetMapping("/api/clientes")
    @ResponseBody
    public ResponseEntity<List<ClienteModel>> listarTodosClientes() {
        return ResponseEntity.ok(clienteService.buscarTodos());
    }

    // 3. API para OBTER POR ID
    @GetMapping("/api/clientes/{id}")
    @ResponseBody
    public ResponseEntity<ClienteModel> obterClientePorId(@PathVariable Long id) {
        try {
            ClienteModel cliente = clienteService.buscarPorId(id);
            return ResponseEntity.ok(cliente);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 4. API para SALVAR (Criar/Atualizar)
    @PostMapping("/api/clientes")
    @ResponseBody
    public ResponseEntity<ClienteModel> salvarOuAtualizarCliente(@RequestBody ClienteModel clienteModel) {
        ClienteModel clienteSalvo = clienteService.salvarOuAtualizar(clienteModel);
        return ResponseEntity.ok(clienteSalvo);
    }

    // 5. API para DELETAR
    @DeleteMapping("/api/clientes/{id}")
    @ResponseBody
    public ResponseEntity<Void> deletarCliente(@PathVariable Long id) {
        try {
            clienteService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 6. API DE PESQUISA (Autocomplete)
    @GetMapping("/api/clientes/pesquisa")
    @ResponseBody
    public ResponseEntity<List<ClienteModel>> pesquisarClientes(@RequestParam("termo") String termo) {
        return ResponseEntity.ok(clienteService.pesquisarPorNomeOuCpf(termo));
    }

    // 7. API DE RELATÓRIO (NOVO)
    @GetMapping("/api/clientes/relatorio")
    @ResponseBody
    public ResponseEntity<Page<ClienteModel>> relatorioClientes(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "dataInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(value = "dataFim", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim
    ) {
        // Ordena por data de cadastro decrescente (mais novos primeiro)
        Pageable pageable = PageRequest.of(page, size, Sort.by("dataCadastro").descending());

        Page<ClienteModel> resultado;

        if (dataInicio != null && dataFim != null) {
            resultado = clienteRepository.findByDataCadastroBetween(dataInicio, dataFim, pageable);
        } else {
            // Se não tem data, retorna todos paginados
            resultado = clienteRepository.findAll(pageable);
        }

        return ResponseEntity.ok(resultado);
    }
}