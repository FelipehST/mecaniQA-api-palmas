package br.com.mecaniQA.api.controller;

import br.com.mecaniQA.api.model.Servico;
import br.com.mecaniQA.api.repository.ServicoRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicos")
public class ServicoController {

    private final ServicoRepository repository = ServicoRepository.getInstance();

    @PostMapping
    public ResponseEntity<Servico> cadastrar(@RequestBody Servico servico) {

        if (!dadosObrigatoriosValidos(servico)) {
            return ResponseEntity.badRequest().build();
        }

        Servico novoServico = repository.salvar(servico);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(novoServico);
    }

    @GetMapping
    public ResponseEntity<List<Servico>> listar() {
        return ResponseEntity.ok(repository.listarTodos());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Servico> buscar(@PathVariable Long codigo) {

        Servico servico = repository.buscarPorCodigo(codigo);

        if (servico == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(servico);
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<Servico> atualizar(
            @PathVariable Long codigo,
            @RequestBody Servico servico) {

        if (!dadosObrigatoriosValidos(servico)) {
            return ResponseEntity.badRequest().build();
        }

        Servico atualizado = repository.atualizar(codigo, servico);

        if (atualizado == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> excluir(@PathVariable Long codigo) {

        boolean excluido = repository.excluir(codigo);

        if (!excluido) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    private boolean dadosObrigatoriosValidos(Servico servico) {

        if (servico.getNome() == null || servico.getNome().isBlank()) {
            return false;
        }

        if (servico.getTempoEstimadoMinutos() == null) {
            return false;
        }

        if (servico.getCustoTabelado() == null) {
            return false;
        }

        return true;
    }
}
