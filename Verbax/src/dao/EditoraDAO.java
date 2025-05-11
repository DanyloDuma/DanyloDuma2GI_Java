// Classe de acesso a dados (DAO) para a entidade Editora.
// Responsável pela execução das operações CRUD (inserir, listar, atualizar, excluir e buscar por ID) na tabela "editora".
package dao;

import model.Editora;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EditoraDAO {

    // Método que insere uma nova editora na base de dados.
    public boolean inserir(Editora editora) {
        // Query SQL para inserir o nome e a cidade na tabela "editora".
        String sql = "INSERT INTO editora (nome, cidade) VALUES (?, ?)";

        // Utiliza try-with-resources para garantir o fecho automático dos recursos (ligação e statement).
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define os parâmetros da query com os valores do objeto Editora.
            stmt.setString(1, editora.getNome());
            stmt.setString(2, editora.getCidade());
            // Executa a query de inserção.
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            // Em caso de erro, regista a mensagem de erro e retorna false.
            System.err.println("Erro ao inserir editora: " + e.getMessage());
            return false;
        }
    }

    // Método que retorna uma lista com todas as editoras registadas na base de dados.
    public List<Editora> listarTodos() {
        // Lista para armazenar as editoras obtidas.
        List<Editora> lista = new ArrayList<>();
        // Query SQL para selecionar todos os registos da tabela "editora".
        String sql = "SELECT * FROM editora";

        // Utiliza try-with-resources para gerir automaticamente a ligação, o statement e o conjunto de resultados.
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Itera por cada registo retornado.
            while (rs.next()) {
                // Cria um novo objeto Editora e define os seus atributos com base nos resultados da query.
                Editora editora = new Editora();
                editora.setId(rs.getInt("id"));
                editora.setNome(rs.getString("nome"));
                editora.setCidade(rs.getString("cidade"));
                // Adiciona o objeto à lista de editoras.
                lista.add(editora);
            }

        } catch (SQLException e) {
            // Em caso de exceção, regista a mensagem de erro.
            System.err.println("Erro ao listar editoras: " + e.getMessage());
        }

        // Retorna a lista de editoras (pode estar vazia se ocorrer algum erro).
        return lista;
    }

    // Método que atualiza os dados de uma editora existente na base de dados.
    public boolean atualizar(Editora editora) {
        // Query SQL para atualizar o nome e a cidade de uma editora identificada pelo seu id.
        String sql = "UPDATE editora SET nome = ?, cidade = ? WHERE id = ?";

        // Utiliza try-with-resources para garantir a gestão automática dos recursos.
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define os parâmetros da query com os novos valores do objeto Editora.
            stmt.setString(1, editora.getNome());
            stmt.setString(2, editora.getCidade());
            stmt.setInt(3, editora.getId());
            // Executa a query de atualização.
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            // Se ocorrer um erro, regista a mensagem de erro e retorna false.
            System.err.println("Erro ao atualizar editora: " + e.getMessage());
            return false;
        }
    }

    // Método que elimina uma editora da base de dados com base no seu identificador.
    public boolean excluir(int id) {
        // Query SQL para eliminar o registo correspondente na tabela "editora".
        String sql = "DELETE FROM editora WHERE id = ?";

        // Utiliza try-with-resources para gerir a ligação e o prepared statement.
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define o parâmetro para a query.
            stmt.setInt(1, id);
            // Executa a query de eliminação.
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            // Em caso de exceção, regista a mensagem de erro e retorna false.
            System.err.println("Erro ao excluir editora: " + e.getMessage());
            return false;
        }
    }

    // Método que procura e retorna uma editora com base no seu identificador.
    public Editora buscarPorId(int id) {
        // Query SQL para selecionar a editora de acordo com o id.
        String sql = "SELECT * FROM editora WHERE id = ?";
        // Variável para armazenar a editora encontrada.
        Editora editora = null;

        // Utiliza try-with-resources para gerir os recursos.
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define o parâmetro id para a query.
            stmt.setInt(1, id);
            // Executa a query e obtém o conjunto de resultados.
            ResultSet rs = stmt.executeQuery();

            // Se um registo for retornado, cria um objeto Editora e define os seus atributos.
            if (rs.next()) {
                editora = new Editora();
                editora.setId(rs.getInt("id"));
                editora.setNome(rs.getString("nome"));
                editora.setCidade(rs.getString("cidade"));
            }

        } catch (SQLException e) {
            // Em caso de exceção, regista a mensagem de erro.
            System.err.println("Erro ao buscar editora por ID: " + e.getMessage());
        }

        // Retorna a editora encontrada ou null se não houver registo.
        return editora;
    }
}
