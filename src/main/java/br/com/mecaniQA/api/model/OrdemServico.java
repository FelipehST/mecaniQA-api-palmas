package br.com.mecaniQA.api.model;

import java.time.LocalDateTime;
import java.util.List;

public class OrdemServico {
    private Long id;
    private String descricaoProblema;
    private LocalDateTime dataAbertura;
    private String status;
    private Double valorTotal;
    private List<Pedido> pedidos;

    public OrdemServico() {
        this.dataAbertura = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescricaoProblema() { return descricaoProblema; }
    public void setDescricaoProblema(String descricaoProblema) { this.descricaoProblema = descricaoProblema; }

    public LocalDateTime getDataAbertura() { return dataAbertura; }
    public void setDataAbertura(LocalDateTime dataAbertura) { this.dataAbertura = dataAbertura; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Double getValorTotal() { return valorTotal; }
    public void setValorTotal(Double valorTotal) { this.valorTotal = valorTotal; }

    public List<Pedido> getPedidos() { return pedidos; }
    public void setPedidos(List<Pedido> pedidos) { this.pedidos = pedidos; }
}