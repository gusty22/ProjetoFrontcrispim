package com.finan.orcamento.controller;

import com.finan.orcamento.model.UsuarioModel;
import com.finan.orcamento.repositories.UsuarioRepository;
import com.finan.orcamento.service.UsuarioService;
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
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/usuarios")
    public String paginaUsuarios() { return "usuarioPage"; }

    @GetMapping("/api/usuarios")
    @ResponseBody
    public ResponseEntity<List<UsuarioModel>> listarTodos() {
        return ResponseEntity.ok(usuarioService.buscarTodos());
    }

    @GetMapping("/api/usuarios/{id}")
    @ResponseBody
    public ResponseEntity<UsuarioModel> buscarPorId(@PathVariable Long id) {
        try { return ResponseEntity.ok(usuarioService.buscarPorId(id)); }
        catch (Exception e) { return ResponseEntity.notFound().build(); }
    }

    @PostMapping("/api/usuarios")
    @ResponseBody
    public ResponseEntity<UsuarioModel> salvar(@RequestBody UsuarioModel usuario) {
        return ResponseEntity.ok(usuarioService.salvar(usuario));
    }

    @DeleteMapping("/api/usuarios/{id}")
    @ResponseBody
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/usuarios/pesquisa")
    @ResponseBody
    public ResponseEntity<List<UsuarioModel>> pesquisar(@RequestParam("nome") String nome) {
        return ResponseEntity.ok(usuarioService.pesquisarPorNome(nome));
    }

    // RELATÓRIO PAGINADO (Data + Valor + Nome)
    @GetMapping("/api/usuarios/relatorio")
    @ResponseBody
    public ResponseEntity<Page<UsuarioModel>> relatorioUsuarios(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "nome", required = false) String nome, // NOVO
            @RequestParam(value = "dataInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(value = "dataFim", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(value = "valorMin", required = false) BigDecimal valorMin,
            @RequestParam(value = "valorMax", required = false) BigDecimal valorMax
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("dataCadastro").descending());
        Page<UsuarioModel> result = usuarioRepository.findUsuariosReport(nome, dataInicio, dataFim, valorMin, valorMax, pageable);
        return ResponseEntity.ok(result);
    }
}