package br.com.mecaniQA.api.repository;

import br.com.mecaniQA.api.model.Peca;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PecaRepository {

    private static PecaRepository instance;

    private final List<Peca> pecas = new ArrayList<>();
    private Long proximoCodigo = 1L;

    private PecaRepository() {
    }

    public static PecaRepository getInstance() {
        if (instance == null) {
            instance = new PecaRepository();
        }
        return instance;
    }

    public Peca salvar(Peca peca) {
        peca.setCodigo(proximoCodigo++);
        peca.setDataCadastro(LocalDateTime.now());
        peca.setDataUltimaAtualizacao(LocalDateTime.now());

        pecas.add(peca);

        return peca;
    }

    public List<Peca> listarTodas() {
        return pecas;
    }

    public Peca buscarPorCodigo(Long codigo) {
        return pecas.stream()
                .filter(peca -> peca.getCodigo().equals(codigo))
                .findFirst()
                .orElse(null);
    }

    public Peca atualizar(Long codigo, Peca dadosAtualizados) {
        Peca pecaExistente = buscarPorCodigo(codigo);

        if (pecaExistente == null) {
            return null;
        }

        pecaExistente.setCodigoBarras(dadosAtualizados.getCodigoBarras());
        pecaExistente.setFornecedorMarca(dadosAtualizados.getFornecedorMarca());
        pecaExistente.setQuantidadeEstoque(dadosAtualizados.getQuantidadeEstoque());
        pecaExistente.setPrecoCusto(dadosAtualizados.getPrecoCusto());
        pecaExistente.setPrecoVenda(dadosAtualizados.getPrecoVenda());
        pecaExistente.setTamanho(dadosAtualizados.getTamanho());
        pecaExistente.setCor(dadosAtualizados.getCor());
        pecaExistente.setCategoria(dadosAtualizados.getCategoria());
        pecaExistente.setDataUltimaAtualizacao(LocalDateTime.now());

        return pecaExistente;
    }

    public boolean excluir(Long codigo) {
        Peca peca = buscarPorCodigo(codigo);

        if (peca == null) {
            return false;
        }

        return pecas.remove(peca);
    }
}
