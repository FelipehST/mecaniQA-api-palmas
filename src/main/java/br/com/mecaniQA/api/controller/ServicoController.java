package br.com.mecaniQA.api.controller;

import br.com.mecaniQA.api.model.Servico;
import br.com.mecaniQA.api.repository.exception.DadosInvalidosException;
import br.com.mecaniQA.api.service.ServicoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/servicos")
public class ServicoController {

    private final ServicoService service;

    public ServicoController(ServicoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Servico servico) {
        try {
            Servico novoServico = service.salvar(servico);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoServico);
        } catch (DadosInvalidosException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("erro", "Erro interno no servidor"));
        }
    }

    @GetMapping
    public ResponseEntity<List<Servico>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<?> buscar(@PathVariable Long codigo) {
        try {
            return ResponseEntity.ok(service.buscarPorCodigo(codigo));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("erro", "Erro interno no servidor"));
        }
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<?> atualizar(@PathVariable Long codigo, @RequestBody Servico servico) {
        try {
            Servico atualizado = service.atualizar(codigo, servico);
            return ResponseEntity.ok(atualizado);
        } catch (DadosInvalidosException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("erro", "Erro interno no servidor"));
        }
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<?> excluir(@PathVariable Long codigo) {
        try {
            service.excluir(codigo);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("erro", "Erro interno no servidor"));
        }
    }
}