package dao;

import model.Editora;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EditoraDAO {

    public boolean inserir(Editora editora) {
        String sql = "INSERT INTO editora (nome, cidade) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, editora.getNome());
            stmt.setString(2, editora.getCidade());
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao inserir editora: " + e.getMessage());
            return false;
        }
    }

    public List<Editora> listarTodos() {
        List<Editora> lista = new ArrayList<>();
        String sql = "SELECT * FROM editora";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Editora editora = new Editora();
                editora.setId(rs.getInt("id"));
                editora.setNome(rs.getString("nome"));
                editora.setCidade(rs.getString("cidade"));
                lista.add(editora);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar editoras: " + e.getMessage());
        }

        return lista;
    }

    public boolean atualizar(Editora editora) {
        String sql = "UPDATE editora SET nome = ?, cidade = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, editora.getNome());
            stmt.setString(2, editora.getCidade());
            stmt.setInt(3, editora.getId());
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar editora: " + e.getMessage());
            return false;
        }
    }

    public boolean excluir(int id) {
        String sql = "DELETE FROM editora WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir editora: " + e.getMessage());
            return false;
        }
    }

    public Editora buscarPorId(int id) {
        String sql = "SELECT * FROM editora WHERE id = ?";
        Editora editora = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                editora = new Editora();
                editora.setId(rs.getInt("id"));
                editora.setNome(rs.getString("nome"));
                editora.setCidade(rs.getString("cidade"));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar editora por ID: " + e.getMessage());
        }

        return editora;
    }
}
