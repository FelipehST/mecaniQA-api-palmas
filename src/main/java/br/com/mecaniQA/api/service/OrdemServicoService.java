package br.com.mecaniQA.api.service;

import br.com.mecaniQA.api.dto.OrdemServicoRequestDTO;
import br.com.mecaniQA.api.dto.OrdemServicoResponseDTO;
import br.com.mecaniQA.api.model.OrdemServico;
import br.com.mecaniQA.api.model.OrdemServicoBuilder;
import br.com.mecaniQA.api.repository.OrdemServicoRepository;
import br.com.mecaniQA.api.repository.exception.DadosInvalidosException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrdemServicoService {

    private final OrdemServicoRepository repository;

    public OrdemServicoService(OrdemServicoRepository repository) {
        this.repository = repository;
    }

    public OrdemServicoResponseDTO criar(OrdemServicoRequestDTO dto) {
        if (dto.getDescricaoProblema() == null || dto.getDescricaoProblema().isBlank()) {
            throw new DadosInvalidosException("A descrição do problema é obrigatória.");
        }

        OrdemServico os = new OrdemServicoBuilder()
                .comDescricao(dto.getDescricaoProblema())
                .comStatus(dto.getStatus() != null ? dto.getStatus() : "ABERTA")
                .comValorTotal(0.0)
                .build();

        os = repository.save(os);
        return converterParaResponse(os);
    }

    public List<OrdemServicoResponseDTO> listarTodas() {
        List<OrdemServicoResponseDTO> lista = new ArrayList<>();
        for (OrdemServico os : repository.findAll()) {
            lista.add(converterParaResponse(os));
        }
        return lista;
    }

    public OrdemServicoResponseDTO buscarPorId(Long id) {
        OrdemServico os = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ordem de Serviço não encontrada."));
        return converterParaResponse(os);
    }

    public OrdemServicoResponseDTO atualizar(Long id, OrdemServicoRequestDTO dto) {
        OrdemServico os = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ordem de Serviço não encontrada."));

        if (dto.getDescricaoProblema() == null || dto.getDescricaoProblema().isBlank()) {
            throw new DadosInvalidosException("A descrição do problema é obrigatória.");
        }

        os.setDescricaoProblema(dto.getDescricaoProblema());
        os.setStatus(dto.getStatus());
        
        OrdemServico atualizada = repository.update(os);
        return converterParaResponse(atualizada);
    }

    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Ordem de Serviço não encontrada.");
        }
        repository.deleteById(id);
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