package br.com.mecaniQA.api.repository;

import br.com.mecaniQA.api.model.OrdemServico;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class OrdemServicoRepository implements GenericRepository<OrdemServico, Long> {

    private final DataSource dataSource;

    public OrdemServicoRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public OrdemServico save(OrdemServico os) {
        String sql = "INSERT INTO ordem_servico (descricao_problema, data_abertura, status, valor_total) VALUES (?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            if (os.getDataAbertura() == null) os.setDataAbertura(LocalDateTime.now());
            if (os.getValorTotal() == null) os.setValorTotal(0.0);

            ps.setString(1, os.getDescricaoProblema());
            ps.setTimestamp(2, Timestamp.valueOf(os.getDataAbertura()));
            ps.setString(3, os.getStatus());
            ps.setDouble(4, os.getValorTotal());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    os.setId(rs.getLong(1));
                }
            }
            return os;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar Ordem de Serviço", e);
        }
    }

    @Override
    public Optional<OrdemServico> findById(Long id) {
        String sql = "SELECT * FROM ordem_servico WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToOrdemServico(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar Ordem de Serviço", e);
        }
    }

    @Override
    public List<OrdemServico> findAll() {
        String sql = "SELECT * FROM ordem_servico";
        List<OrdemServico> lista = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapResultSetToOrdemServico(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar Ordens de Serviço", e);
        }
    }

    @Override
    public OrdemServico update(OrdemServico os) {
        String sql = "UPDATE ordem_servico SET descricao_problema=?, status=?, valor_total=? WHERE id=?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, os.getDescricaoProblema());
            ps.setString(2, os.getStatus());
            ps.setDouble(3, os.getValorTotal() != null ? os.getValorTotal() : 0.0);
            ps.setLong(4, os.getId());

            int rows = ps.executeUpdate();
            if (rows == 0) throw new RuntimeException("Ordem de Serviço não encontrada");
            
            return findById(os.getId()).orElseThrow();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar Ordem de Serviço", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM ordem_servico WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar Ordem de Serviço", e);
        }
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT 1 FROM ordem_servico WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar Ordem de Serviço", e);
        }
    }

    private OrdemServico mapResultSetToOrdemServico(ResultSet rs) throws SQLException {
        OrdemServico os = new OrdemServico();
        os.setId(rs.getLong("id"));
        os.setDescricaoProblema(rs.getString("descricao_problema"));
        
        Timestamp ts = rs.getTimestamp("data_abertura");
        if (ts != null) os.setDataAbertura(ts.toLocalDateTime());
        
        os.setStatus(rs.getString("status"));
        os.setValorTotal(rs.getDouble("valor_total"));
        return os;
    }
}