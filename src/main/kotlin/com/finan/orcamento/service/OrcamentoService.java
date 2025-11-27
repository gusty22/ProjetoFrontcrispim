package com.finan.orcamento.service;

import com.finan.orcamento.model.ClienteModel;
import com.finan.orcamento.model.OrcamentoModel;
import com.finan.orcamento.model.UsuarioModel;
import com.finan.orcamento.repositories.ClienteRepository;
import com.finan.orcamento.repositories.OrcamentoRepository;
import com.finan.orcamento.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrcamentoService {
    @Autowired
    private OrcamentoRepository orcamentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public List<OrcamentoModel> buscarCadastro(){
        return orcamentoRepository.findAll();
    }

    public OrcamentoModel buscaId(Long id){
        Optional<OrcamentoModel> obj = orcamentoRepository.findById(id);
        return obj.orElseThrow(() -> new RuntimeException("Orçamento com ID " + id + " não encontrado"));
    }

    // Método privado para validar e preencher
    private OrcamentoModel validarEPreencherCampos(OrcamentoModel orcamento) {
        boolean hasUsuario = orcamento.getUsuario() != null && orcamento.getUsuario().getId() != null;
        boolean hasCliente = orcamento.getCliente() != null && orcamento.getCliente().getId() != null;

        if (!hasUsuario && !hasCliente) {
            throw new RuntimeException("Orçamento deve estar associado a pelo menos um Usuário ou Cliente.");
        }

        if (hasUsuario) {
            Long uid = orcamento.getUsuario().getId();
            UsuarioModel u = usuarioRepository.findById(uid)
                    .orElseThrow(() -> new RuntimeException("Usuário ID " + uid + " não encontrado"));
            orcamento.setUsuario(u);
        } else {
            orcamento.setUsuario(null);
        }

        if (hasCliente) {
            Long cid = orcamento.getCliente().getId();
            ClienteModel c = clienteRepository.findById(cid)
                    .orElseThrow(() -> new RuntimeException("Cliente ID " + cid + " não encontrado"));
            orcamento.setCliente(c);
        } else {
            orcamento.setCliente(null);
        }

        orcamento.calcularIcms();
        return orcamento;
    }

    public OrcamentoModel salvarOuAtualizar(OrcamentoModel orcamentoModel) {
        OrcamentoModel validado = validarEPreencherCampos(orcamentoModel);

        if (orcamentoModel.getId() != null) {
            OrcamentoModel existente = buscaId(orcamentoModel.getId());
            existente.setIcmsEstados(validado.getIcmsEstados());
            existente.setValorOrcamento(validado.getValorOrcamento());
            existente.setValorICMS(validado.getValorICMS());
            existente.setUsuario(validado.getUsuario());
            existente.setCliente(validado.getCliente());
            return orcamentoRepository.save(existente);
        } else {
            return orcamentoRepository.save(validado);
        }
    }

    public void deletaOrcamento(Long id){
        if (!orcamentoRepository.existsById(id)) {
            throw new RuntimeException("Orçamento não encontrado.");
        }
        orcamentoRepository.deleteById(id);
    }
}