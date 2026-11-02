package br.com.mecaniQA.api.service;

import br.com.mecaniQA.api.model.Servico;
import br.com.mecaniQA.api.repository.ServicoRepository;
import br.com.mecaniQA.api.repository.exception.DadosInvalidosException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicoService {

    private final ServicoRepository repository;

    public ServicoService(ServicoRepository repository) {
        this.repository = repository;
    }

    public Servico salvar(Servico servico) {
        validarDados(servico);
        return repository.save(servico);
    }

    public List<Servico> listarTodos() {
        return repository.findAll();
    }

    public Servico buscarPorCodigo(Long codigo) {
        return repository.findById(codigo)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));
    }

    public Servico atualizar(Long codigo, Servico servico) {
        validarDados(servico);
        if (!repository.existsById(codigo)) {
            throw new RuntimeException("Serviço não encontrado");
        }
        servico.setCodigo(codigo);
        return repository.update(servico);
    }

    public void excluir(Long codigo) {
        if (!repository.existsById(codigo)) {
            throw new RuntimeException("Serviço não encontrado");
        }
        repository.deleteById(codigo);
    }

    private void validarDados(Servico servico) {
        if (servico.getNome() == null || servico.getNome().isBlank()) {
            throw new DadosInvalidosException("O nome do serviço é obrigatório.");
        }
        if (servico.getTempoEstimadoMinutos() == null || servico.getTempoEstimadoMinutos() <= 0) {
            throw new DadosInvalidosException("O tempo estimado deve ser maior que zero.");
        }
    }
}