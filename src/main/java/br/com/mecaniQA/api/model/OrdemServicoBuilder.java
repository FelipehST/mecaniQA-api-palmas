package br.com.mecaniQA.api.model;

public class OrdemServicoBuilder {
    private OrdemServico ordemServico;

    public OrdemServicoBuilder() {
        this.ordemServico = new OrdemServico();
    }

    public OrdemServicoBuilder comDescricao(String descricao) {
        this.ordemServico.setDescricaoProblema(descricao);
        return this;
    }

    public OrdemServicoBuilder comStatus(String status) {
        this.ordemServico.setStatus(status);
        return this;
    }

    public OrdemServicoBuilder comValorTotal(Double valor) {
        this.ordemServico.setValorTotal(valor);
        return this;
    }

    public OrdemServico build() {
        return this.ordemServico;
    }
}