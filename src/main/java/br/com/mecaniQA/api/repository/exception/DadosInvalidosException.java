package br.com.mecaniQA.api.repository.exception;

public class DadosInvalidosException extends RuntimeException {
    public DadosInvalidosException(String message) {
        super(message);
    }
}