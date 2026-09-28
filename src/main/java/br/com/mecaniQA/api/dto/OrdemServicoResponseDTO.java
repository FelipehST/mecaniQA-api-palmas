package br.com.mecaniQA.api.dto;

public class OrdemServicoResponseDTO {
    private Long id;
    private String descricaoProblema;
    private String status;
    private Double valorTotal;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescricaoProblema() { return descricaoProblema; }
    public void setDescricaoProblema(String descricaoProblema) { this.descricaoProblema = descricaoProblema; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Double getValorTotal() { return valorTotal; }
    public void setValorTotal(Double valorTotal) { this.valorTotal = valorTotal; }
}