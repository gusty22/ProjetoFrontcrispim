package com.finan.orcamento.controller;

import com.finan.orcamento.model.UsuarioModel;
import com.finan.orcamento.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // 1. Endpoint para carregar a PÁGINA HTML principal
    @GetMapping("/usuarios")
    public String carregarPaginaPrincipal() {
        // Retorna o nome do seu arquivo HTML. Renomeie seu arquivo para "gerenciador.html"
        // ou mude o nome aqui para "usuarioPage".
        return "usuarioPage";
    }

    // --- A PARTIR DAQUI, TODOS OS ENDPOINTS SÃO APIs QUE RETORNAM JSON ---

    // 2. API para LISTAR todos os usuários (para preencher a tabela)
    @GetMapping("/api/usuarios")
    @ResponseBody
    public ResponseEntity<List<UsuarioModel>> listarTodosUsuarios() {
        return ResponseEntity.ok(usuarioService.buscarUsuario());
    }

    // 3. API para BUSCAR um usuário por NOME
    // Retorna uma lista, mas o frontend pegará o primeiro resultado.
    @GetMapping("/api/usuarios/pesquisa")
    @ResponseBody
    public ResponseEntity<List<UsuarioModel>> pesquisarPorNome(@RequestParam String nome) {
        List<UsuarioModel> usuarios = usuarioService.buscarUsuarioPorNome(nome);
        return ResponseEntity.ok(usuarios);
    }

    // 4. API para OBTER um usuário específico por ID (para o botão "Editar")
    @GetMapping("/api/usuarios/{id}")
    @ResponseBody
    public ResponseEntity<UsuarioModel> obterUsuarioPorId(@PathVariable Long id) {
        try {
            UsuarioModel usuario = usuarioService.buscaId(id);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 5. API para SALVAR (criar ou atualizar) um usuário
    @PostMapping("/api/usuarios")
    @ResponseBody
    public ResponseEntity<UsuarioModel> salvarOuAtualizarUsuario(@RequestBody UsuarioModel usuarioModel) {
        // Usamos @RequestBody porque o JavaScript enviará os dados como JSON
        UsuarioModel usuarioSalvo = usuarioService.salvarOuAtualizar(usuarioModel);
        return ResponseEntity.ok(usuarioSalvo);
    }

    // 6. API para DELETAR um usuário
    @DeleteMapping("/api/usuarios/{id}")
    @ResponseBody
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        try {
            usuarioService.deletaUsuario(id);
            return ResponseEntity.noContent().build(); // Retorna status 204 (Sucesso, sem conteúdo)
        } catch (Exception e) {
            // Caso ocorra erro (ex: usuário não existe), retorna 404 ou 500
            return ResponseEntity.notFound().build();
        }
    }
}
