package br.com.mecaniQA.api.model;

import java.util.List;

public class Pedido {
    private Long id;
    private String descricao;
    private Double valor;
    private List<ItemPedido> itens;

}