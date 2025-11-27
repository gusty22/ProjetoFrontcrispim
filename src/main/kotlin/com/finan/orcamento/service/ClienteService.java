package com.finan.orcamento.service;

import com.finan.orcamento.model.ClienteModel;
import com.finan.orcamento.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<ClienteModel> buscarTodos() {
        return clienteRepository.findAll();
    }

    public ClienteModel buscarPorId(Long id) {
        Optional<ClienteModel> obj = clienteRepository.findById(id);
        return obj.orElseThrow(() -> new RuntimeException("Cliente com ID " + id + " não encontrado"));
    }

    public ClienteModel salvarOuAtualizar(ClienteModel clienteModel) {
        if (clienteModel.getId() != null) {
            ClienteModel existente = buscarPorId(clienteModel.getId());
            existente.setNome(clienteModel.getNome());
            existente.setCpf(clienteModel.getCpf());
            return clienteRepository.save(existente);
        }
        return clienteRepository.save(clienteModel);
    }

    public void deletar(Long id) {
        clienteRepository.deleteById(id);
    }

    public List<ClienteModel> pesquisarPorNomeOuCpf(String termo) {
        return clienteRepository.findByNomeContainingIgnoreCaseOrCpfContaining(termo, termo);
    }
}