package br.com.mecaniQA.api.dto;

public class OrdemServicoRequestDTO {
    private String descricaoProblema;
    private String status;

    public String getDescricaoProblema() { return descricaoProblema; }
    public void setDescricaoProblema(String descricaoProblema) { this.descricaoProblema = descricaoProblema; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}