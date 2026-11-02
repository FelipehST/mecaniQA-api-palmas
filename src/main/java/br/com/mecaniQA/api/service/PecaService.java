package br.com.mecaniQA.api.service;

import br.com.mecaniQA.api.model.Peca;
import br.com.mecaniQA.api.repository.PecaRepository;
import br.com.mecaniQA.api.repository.exception.DadosInvalidosException;
import br.com.mecaniQA.api.repository.exception.PecaNaoEncontradaException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PecaService {

    private final PecaRepository repository;

    public PecaService(PecaRepository repository) {
        this.repository = repository;
    }

    public Peca salvar(Peca peca) {
        validarDados(peca);
        return repository.save(peca);
    }

    public List<Peca> listarTodas() {
        return repository.findAll();
    }

    public Peca buscarPorCodigo(Long codigo) {
        return repository.findById(codigo)
                .orElseThrow(() -> new PecaNaoEncontradaException(codigo));
    }

    public Peca atualizar(Long codigo, Peca peca) {
        validarDados(peca);
        if (!repository.existsById(codigo)) {
            throw new PecaNaoEncontradaException(codigo);
        }
        peca.setCodigo(codigo);
        return repository.update(peca);
    }

    public void excluir(Long codigo) {
        if (!repository.existsById(codigo)) {
            throw new PecaNaoEncontradaException(codigo);
        }
        repository.deleteById(codigo);
    }

    private void validarDados(Peca peca) {
        if (peca.getNome() == null || peca.getNome().isBlank()) {
            throw new DadosInvalidosException("O nome da peça é obrigatório.");
        }
        if (peca.getCodigoBarras() == null || peca.getCodigoBarras().isBlank()) {
            throw new DadosInvalidosException("O código de barras é obrigatório.");
        }
        if (peca.getPrecoVenda() == null || peca.getPrecoVenda() < 0) {
            throw new DadosInvalidosException("O preço de venda não pode ser nulo ou negativo.");
        }
    }
}