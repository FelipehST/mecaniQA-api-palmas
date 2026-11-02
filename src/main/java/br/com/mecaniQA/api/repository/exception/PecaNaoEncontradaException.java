package br.com.mecaniQA.api.repository.exception;

public class PecaNaoEncontradaException extends RuntimeException {
    public PecaNaoEncontradaException(Long codigo) {
        super("Peça não encontrada com o código: " + codigo);
    }
}