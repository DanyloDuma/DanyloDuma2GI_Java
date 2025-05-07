package dao;

import model.Tema;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TemaDAO {

    public boolean inserir(Tema tema) {
        String sql = "INSERT INTO tema (nome) VALUES (?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tema.getNome());
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao inserir tema: " + e.getMessage());
            return false;
        }
    }

    public List<Tema> listarTodos() {
        List<Tema> lista = new ArrayList<>();
        String sql = "SELECT * FROM tema";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Tema tema = new Tema();
                tema.setId(rs.getInt("id"));
                tema.setNome(rs.getString("nome"));
                lista.add(tema);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar temas: " + e.getMessage());
        }

        return lista;
    }

    public boolean atualizar(Tema tema) {
        String sql = "UPDATE tema SET nome = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tema.getNome());
            stmt.setInt(2, tema.getId());
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar tema: " + e.getMessage());
            return false;
        }
    }

    public boolean excluir(int id) {
        String sql = "DELETE FROM tema WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir tema: " + e.getMessage());
            return false;
        }
    }

    public Tema buscarPorId(int id) {
        String sql = "SELECT * FROM tema WHERE id = ?";
        Tema tema = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                tema = new Tema();
                tema.setId(rs.getInt("id"));
                tema.setNome(rs.getString("nome"));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar tema: " + e.getMessage());
        }

        return tema;
    }
}
