package br.com.mecaniQA.api.repository;

import br.com.mecaniQA.api.model.Servico;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ServicoRepository implements GenericRepository<Servico, Long> {

    private final DataSource dataSource;

    public ServicoRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Servico save(Servico servico) {
        String sql = "INSERT INTO servico (nome, tempo_estimado_minutos, custo_tabelado, data_criacao, data_ultima_atualizacao) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            servico.setDataCriacao(LocalDateTime.now());
            servico.setDataUltimaAtualizacao(LocalDateTime.now());

            ps.setString(1, servico.getNome());
            ps.setInt(2, servico.getTempoEstimadoMinutos());
            ps.setDouble(3, servico.getCustoTabelado());
            ps.setTimestamp(4, Timestamp.valueOf(servico.getDataCriacao()));
            ps.setTimestamp(5, Timestamp.valueOf(servico.getDataUltimaAtualizacao()));

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    servico.setCodigo(rs.getLong(1));
                }
            }
            return servico;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar serviço", e);
        }
    }

    @Override
    public Optional<Servico> findById(Long codigo) {
        String sql = "SELECT * FROM servico WHERE codigo = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToServico(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar serviço", e);
        }
    }

    @Override
    public List<Servico> findAll() {
        String sql = "SELECT * FROM servico";
        List<Servico> servicos = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                servicos.add(mapResultSetToServico(rs));
            }
            return servicos;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar serviços", e);
        }
    }

    @Override
    public Servico update(Servico servico) {
        String sql = "UPDATE servico SET nome=?, tempo_estimado_minutos=?, custo_tabelado=?, data_ultima_atualizacao=? WHERE codigo=?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, servico.getNome());
            ps.setInt(2, servico.getTempoEstimadoMinutos());
            ps.setDouble(3, servico.getCustoTabelado());
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            ps.setLong(5, servico.getCodigo());

            int rows = ps.executeUpdate();
            if (rows == 0) throw new RuntimeException("Serviço não encontrado");
            
            return findById(servico.getCodigo()).orElseThrow();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar serviço", e);
        }
    }

    @Override
    public void deleteById(Long codigo) {
        String sql = "DELETE FROM servico WHERE codigo = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, codigo);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar serviço", e);
        }
    }

    @Override
    public boolean existsById(Long codigo) {
        String sql = "SELECT 1 FROM servico WHERE codigo = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar serviço", e);
        }
    }

    private Servico mapResultSetToServico(ResultSet rs) throws SQLException {
        Servico servico = new Servico();
        servico.setCodigo(rs.getLong("codigo"));
        servico.setNome(rs.getString("nome"));
        servico.setTempoEstimadoMinutos(rs.getInt("tempo_estimado_minutos"));
        servico.setCustoTabelado(rs.getDouble("custo_tabelado"));
        
        Timestamp tsCriacao = rs.getTimestamp("data_criacao");
        if (tsCriacao != null) servico.setDataCriacao(tsCriacao.toLocalDateTime());
        
        Timestamp tsAtualizacao = rs.getTimestamp("data_ultima_atualizacao");
        if (tsAtualizacao != null) servico.setDataUltimaAtualizacao(tsAtualizacao.toLocalDateTime());
        
        return servico;
    }
}