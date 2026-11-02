package br.com.mecaniQA.api.controller;

import br.com.mecaniQA.api.model.Peca;
import br.com.mecaniQA.api.repository.exception.DadosInvalidosException;
import br.com.mecaniQA.api.repository.exception.PecaNaoEncontradaException;
import br.com.mecaniQA.api.service.PecaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pecas")
public class PecaController {

    private final PecaService service;

    public PecaController(PecaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Peca peca) {
        try {
            Peca novaPeca = service.salvar(peca);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaPeca);
        } catch (DadosInvalidosException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("erro", "Erro interno no servidor"));
        }
    }

    @GetMapping
    public ResponseEntity<List<Peca>> listar() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<?> buscar(@PathVariable Long codigo) {
        try {
            return ResponseEntity.ok(service.buscarPorCodigo(codigo));
        } catch (PecaNaoEncontradaException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("erro", "Erro interno no servidor"));
        }
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<?> atualizar(@PathVariable Long codigo, @RequestBody Peca peca) {
        try {
            Peca atualizada = service.atualizar(codigo, peca);
            return ResponseEntity.ok(atualizada);
        } catch (PecaNaoEncontradaException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", e.getMessage()));
        } catch (DadosInvalidosException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("erro", "Erro interno no servidor"));
        }
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<?> excluir(@PathVariable Long codigo) {
        try {
            service.excluir(codigo);
            return ResponseEntity.noContent().build();
        } catch (PecaNaoEncontradaException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("erro", "Erro interno no servidor"));
        }
    }
}