package br.com.mecaniQA.api.controller;

import br.com.mecaniQA.api.model.Peca;
import br.com.mecaniQA.api.repository.PecaRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pecas")
public class PecaController {

    private final PecaRepository repository = PecaRepository.getInstance();

    @PostMapping
    public ResponseEntity<Peca> cadastrar(@RequestBody Peca peca) {

        if (peca.getCategoria() == null) {
            return ResponseEntity.badRequest().build();
        }

        Peca novaPeca = repository.salvar(peca);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(novaPeca);
    }

    @GetMapping
    public ResponseEntity<List<Peca>> listar() {
        return ResponseEntity.ok(repository.listarTodas());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Peca> buscar(@PathVariable Long codigo) {

        Peca peca = repository.buscarPorCodigo(codigo);

        if (peca == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(peca);
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<Peca> atualizar(
            @PathVariable Long codigo,
            @RequestBody Peca peca) {

        if (peca.getCategoria() == null) {
            return ResponseEntity.badRequest().build();
        }

        Peca atualizada = repository.atualizar(codigo, peca);

        if (atualizada == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(atualizada);
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> excluir(@PathVariable Long codigo) {

        boolean excluida = repository.excluir(codigo);

        if (!excluida) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
