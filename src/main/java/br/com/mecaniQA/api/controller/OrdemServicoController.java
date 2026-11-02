package br.com.mecaniQA.api.controller;

import br.com.mecaniQA.api.dto.OrdemServicoRequestDTO;
import br.com.mecaniQA.api.dto.OrdemServicoResponseDTO;
import br.com.mecaniQA.api.repository.exception.DadosInvalidosException;
import br.com.mecaniQA.api.service.OrdemServicoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordens")
public class OrdemServicoController {

    private final OrdemServicoService service;

    public OrdemServicoController(OrdemServicoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody OrdemServicoRequestDTO dto) {
        try {
            OrdemServicoResponseDTO response = service.criar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (DadosInvalidosException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("erro", "Erro interno no servidor"));
        }
    }

    @GetMapping
    public ResponseEntity<List<OrdemServicoResponseDTO>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.buscarPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("erro", "Erro interno no servidor"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody OrdemServicoRequestDTO dto) {
        try {
            OrdemServicoResponseDTO response = service.atualizar(id, dto);
            return ResponseEntity.ok(response);
        } catch (DadosInvalidosException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("erro", "Erro interno no servidor"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        try {
            service.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("erro", "Erro interno no servidor"));
        }
    }
}