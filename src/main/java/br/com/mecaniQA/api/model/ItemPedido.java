package br.com.mecaniQA.api.model;

public class ItemPedido {
    private Long id;
    private Integer quantidade;
    private Double precoUnitario;
    private Peca peca; // Pode ser nulo se for serviço
    private Servico servico; // Pode ser nulo se for peça

}