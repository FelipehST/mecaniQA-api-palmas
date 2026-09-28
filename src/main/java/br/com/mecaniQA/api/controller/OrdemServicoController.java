package br.com.mecaniQA.api.controller;

import br.com.mecaniQA.api.dto.OrdemServicoRequestDTO;
import br.com.mecaniQA.api.dto.OrdemServicoResponseDTO;
import br.com.mecaniQA.api.model.OrdemServico;
import br.com.mecaniQA.api.model.OrdemServicoBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ordens")
public class OrdemServicoController {

    private List<OrdemServico> bancoDados = new ArrayList<>();
    private Long proximoId = 1L;

    @PostMapping
    public OrdemServicoResponseDTO criar(@RequestBody OrdemServicoRequestDTO dto) {
        OrdemServico os = new OrdemServicoBuilder()
                .comDescricao(dto.getDescricaoProblema())
                .comStatus(dto.getStatus())
                .comValorTotal(0.0)
                .build();

        os.setId(proximoId++);
        bancoDados.add(os);

        return converterParaResponse(os);
    }

    @GetMapping
    public List<OrdemServicoResponseDTO> listarTodas() {
        List<OrdemServicoResponseDTO> lista = new ArrayList<>();
        for (OrdemServico os : bancoDados) {
            lista.add(converterParaResponse(os));
        }
        return lista;
    }

    @GetMapping("/{id}")
    public OrdemServicoResponseDTO buscarPorId(@PathVariable Long id) {
        for (OrdemServico os : bancoDados) {
            if (os.getId().equals(id)) {
                return converterParaResponse(os);
            }
        }
        return null;
    }

    @PutMapping("/{id}")
    public OrdemServicoResponseDTO atualizar(@PathVariable Long id, @RequestBody OrdemServicoRequestDTO dto) {
        for (OrdemServico os : bancoDados) {
            if (os.getId().equals(id)) {
                os.setDescricaoProblema(dto.getDescricaoProblema());
                os.setStatus(dto.getStatus());
                return converterParaResponse(os);
            }
        }
        return null;
    }

    private OrdemServicoResponseDTO converterParaResponse(OrdemServico os) {
        OrdemServicoResponseDTO response = new OrdemServicoResponseDTO();
        response.setId(os.getId());
        response.setDescricaoProblema(os.getDescricaoProblema());
        response.setStatus(os.getStatus());
        response.setValorTotal(os.getValorTotal());
        return response;
    }
}