package dao;

import model.Localizacao;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocalizacaoDAO {

    public boolean inserir(Localizacao loc) {
        String sql = "INSERT INTO localizacao (setor, prateleira) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, loc.getSetor());
            stmt.setString(2, loc.getPrateleira());
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao inserir localizacao: " + e.getMessage());
            return false;
        }
    }

    public List<Localizacao> listarTodos() {
        List<Localizacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM localizacao";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Localizacao loc = new Localizacao();
                loc.setId(rs.getInt("id"));
                loc.setSetor(rs.getString("setor"));
                loc.setPrateleira(rs.getString("prateleira"));
                lista.add(loc);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar localizacoes: " + e.getMessage());
        }

        return lista;
    }

    public boolean atualizar(Localizacao loc) {
        String sql = "UPDATE localizacao SET setor = ?, prateleira = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, loc.getSetor());
            stmt.setString(2, loc.getPrateleira());
            stmt.setInt(3, loc.getId());
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar localizacao: " + e.getMessage());
            return false;
        }
    }

    public boolean excluir(int id) {
        String sql = "DELETE FROM localizacao WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir localizacao: " + e.getMessage());
            return false;
        }
    }

    public Localizacao buscarPorId(int id) {
        String sql = "SELECT * FROM localizacao WHERE id = ?";
        Localizacao loc = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                loc = new Localizacao();
                loc.setId(rs.getInt("id"));
                loc.setSetor(rs.getString("setor"));
                loc.setPrateleira(rs.getString("prateleira"));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar localizacao por ID: " + e.getMessage());
        }

        return loc;
    }
}
