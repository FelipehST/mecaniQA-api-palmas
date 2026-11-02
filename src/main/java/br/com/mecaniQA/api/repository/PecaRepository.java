package br.com.mecaniQA.api.repository;

import br.com.mecaniQA.api.model.CategoriaPeca;
import br.com.mecaniQA.api.model.Peca;
import br.com.mecaniQA.api.repository.exception.PecaNaoEncontradaException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PecaRepository implements GenericRepository<Peca, Long> {

    private final DataSource dataSource;

    public PecaRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Peca save(Peca peca) {
     String sql = "INSERT INTO peca " + "(nome, codigo_barras, fornecedor_marca, quantidade_estoque, preco_custo, preco_venda, data_cadastro, data_ultima_atualizacao, tamanho, cor, categoria) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            peca.setDataCadastro(LocalDateTime.now());
            peca.setDataUltimaAtualizacao(LocalDateTime.now());

            ps.setString(1, peca.getNome());
            ps.setString(2, peca.getCodigoBarras());
            ps.setString(3, peca.getFornecedorMarca());
            ps.setInt(4, peca.getQuantidadeEstoque());
            ps.setDouble(5, peca.getPrecoCusto());
            ps.setDouble(6, peca.getPrecoVenda());
            ps.setTimestamp(7, Timestamp.valueOf(peca.getDataCadastro()));
            ps.setTimestamp(8, Timestamp.valueOf(peca.getDataUltimaAtualizacao()));
            ps.setString(9, peca.getTamanho());
            ps.setString(10, peca.getCor());
            ps.setString(11, peca.getCategoria() != null ? peca.getCategoria().name() : null);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    peca.setCodigo(rs.getLong(1));
                }
            }
            return peca;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar peça", e);
        }
    }

    @Override
    public Optional<Peca> findById(Long codigo) {
        String sql = "SELECT * FROM peca WHERE codigo = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToPeca(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar peça", e);
        }
    }

    @Override
    public List<Peca> findAll() {
        String sql = "SELECT * FROM peca";
        List<Peca> pecas = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                pecas.add(mapResultSetToPeca(rs));
            }
            return pecas;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar peças", e);
        }
    }

    @Override
    public Peca update(Peca peca) {
        String sql = "UPDATE peca SET nome=?, codigo_barras=?, fornecedor_marca=?, quantidade_estoque=?, preco_custo=?, preco_venda=?, data_ultima_atualizacao=?, tamanho=?, cor=?, categoria=? WHERE codigo=?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, peca.getNome());
            ps.setString(2, peca.getCodigoBarras());
            ps.setString(3, peca.getFornecedorMarca());
            ps.setInt(4, peca.getQuantidadeEstoque());
            ps.setDouble(5, peca.getPrecoCusto());
            ps.setDouble(6, peca.getPrecoVenda());
            ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(8, peca.getTamanho());
            ps.setString(9, peca.getCor());
            ps.setString(10, peca.getCategoria() != null ? peca.getCategoria().name() : null);
            ps.setLong(11, peca.getCodigo());

            int rows = ps.executeUpdate();
            if (rows == 0) throw new PecaNaoEncontradaException(peca.getCodigo());
            
            return findById(peca.getCodigo()).orElseThrow();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar peça", e);
        }
    }

    @Override
    public void deleteById(Long codigo) {
        String sql = "DELETE FROM peca WHERE codigo = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, codigo);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar peça", e);
        }
    }

    @Override
    public boolean existsById(Long codigo) {
        String sql = "SELECT 1 FROM peca WHERE codigo = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar peça", e);
        }
    }

    private Peca mapResultSetToPeca(ResultSet rs) throws SQLException {
        Peca peca = new Peca();
        peca.setCodigo(rs.getLong("codigo"));
        peca.setNome(rs.getString("nome"));
        peca.setCodigoBarras(rs.getString("codigo_barras"));
        peca.setFornecedorMarca(rs.getString("fornecedor_marca"));
        peca.setQuantidadeEstoque(rs.getInt("quantidade_estoque"));
        peca.setPrecoCusto(rs.getDouble("preco_custo"));
        peca.setPrecoVenda(rs.getDouble("preco_venda"));
        
        Timestamp tsCadastro = rs.getTimestamp("data_cadastro");
        if (tsCadastro != null) peca.setDataCadastro(tsCadastro.toLocalDateTime());
        
        Timestamp tsAtualizacao = rs.getTimestamp("data_ultima_atualizacao");
        if (tsAtualizacao != null) peca.setDataUltimaAtualizacao(tsAtualizacao.toLocalDateTime());
        
        peca.setTamanho(rs.getString("tamanho"));
        peca.setCor(rs.getString("cor"));
        
        String cat = rs.getString("categoria");
        if (cat != null) peca.setCategoria(CategoriaPeca.valueOf(cat));
        
        return peca;
    }
}