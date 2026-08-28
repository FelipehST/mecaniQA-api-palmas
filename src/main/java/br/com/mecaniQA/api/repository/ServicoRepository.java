package br.com.mecaniQA.api.repository;

import br.com.mecaniQA.api.model.Servico;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServicoRepository {

    private static ServicoRepository instance;

    private final List<Servico> servicos = new ArrayList<>();
    private Long proximoCodigo = 1L;

    private ServicoRepository() {
    }

    public static ServicoRepository getInstance() {
        if (instance == null) {
            instance = new ServicoRepository();
        }
        return instance;
    }

    public Servico salvar(Servico servico) {
        servico.setCodigo(proximoCodigo++);
        servico.setDataCriacao(LocalDateTime.now());
        servico.setDataUltimaAtualizacao(LocalDateTime.now());

        servicos.add(servico);

        return servico;
    }

    public List<Servico> listarTodos() {
        return servicos;
    }

    public Servico buscarPorCodigo(Long codigo) {
        return servicos.stream()
                .filter(servico -> servico.getCodigo().equals(codigo))
                .findFirst()
                .orElse(null);
    }

    public Servico atualizar(Long codigo, Servico dadosAtualizados) {
        Servico servicoExistente = buscarPorCodigo(codigo);

        if (servicoExistente == null) {
            return null;
        }

        servicoExistente.setNome(dadosAtualizados.getNome());
        servicoExistente.setTempoEstimadoMinutos(
                dadosAtualizados.getTempoEstimadoMinutos()
        );
        servicoExistente.setCustoTabelado(dadosAtualizados.getCustoTabelado());
        servicoExistente.setDataUltimaAtualizacao(LocalDateTime.now());

        return servicoExistente;
    }

    public boolean excluir(Long codigo) {
        Servico servico = buscarPorCodigo(codigo);

        if (servico == null) {
            return false;
        }

        return servicos.remove(servico);
    }
}
