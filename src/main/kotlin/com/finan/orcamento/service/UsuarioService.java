package com.finan.orcamento.service;

import com.finan.orcamento.model.UsuarioModel;
import com.finan.orcamento.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<UsuarioModel> buscarTodos() {
        return usuarioRepository.findAll();
    }

    public UsuarioModel buscarPorId(Long id) {
        Optional<UsuarioModel> obj = usuarioRepository.findById(id);
        return obj.orElseThrow(() -> new RuntimeException("Usuário com ID " + id + " não encontrado"));
    }

    public UsuarioModel salvar(UsuarioModel usuario) {
        if (usuario.getId() != null) {
            UsuarioModel existente = buscarPorId(usuario.getId());
            existente.setNomeUsuario(usuario.getNomeUsuario());
            existente.setCpf(usuario.getCpf());
            existente.setRg(usuario.getRg());
            existente.setNomeMae(usuario.getNomeMae());
            return usuarioRepository.save(existente);
        }
        return usuarioRepository.save(usuario);
    }

    public void deletar(Long id) {
        usuarioRepository.deleteById(id);
    }

    public List<UsuarioModel> pesquisarPorNome(String nome) {
        return usuarioRepository.findByNomeUsuarioContainingIgnoreCase(nome);
    }
}