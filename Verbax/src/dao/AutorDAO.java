// Classe de acesso a dados (DAO) para a entidade Autor.
// Responsável pela execução das operações CRUD (Criar, Ler, Atualizar, Eliminar) relativas à tabela "autor" na base de dados.
package dao;

import model.Autor;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AutorDAO {

    // Método que insere um novo Autor na base de dados.
    public boolean inserir(Autor autor) {
        // Query SQL para inserir o nome e a nacionalidade do autor.
        String sql = "INSERT INTO autor (nome, nacionalidade) VALUES (?, ?)";

        // Utiliza try-with-resources para garantir o fecho automático dos recursos (ligação e statement).
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define os parâmetros da query usando os valores do objeto Autor.
            stmt.setString(1, autor.getNome());
            stmt.setString(2, autor.getNacionalidade());

            // Executa a instrução de inserção na base de dados.
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            // Em caso de erro, regista a mensagem no console e retorna false.
            System.err.println("Erro ao inserir autor: " + e.getMessage());
            return false;
        }
    }

    // Método que retorna uma lista com todos os Autores existentes na base de dados.
    public List<Autor> listarTodos() {
        // Lista para armazenar os autores obtidos.
        List<Autor> lista = new ArrayList<>();
        // Query SQL para selecionar todos os registos da tabela "autor".
        String sql = "SELECT * FROM autor";

        // Utiliza try-with-resources para gerir os recursos de ligação, statement e result set.
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Itera sobre os registos retornados no ResultSet.
            while (rs.next()) {
                // Cria uma nova instância de Autor e atribui os dados do registo.
                Autor autor = new Autor();
                autor.setId(rs.getInt("id"));
                autor.setNome(rs.getString("nome"));
                autor.setNacionalidade(rs.getString("nacionalidade"));
                // Adiciona o objeto Autor à lista.
                lista.add(autor);
            }

        } catch (SQLException e) {
            // Em caso de erro, regista a mensagem no console.
            System.err.println("Erro ao listar autores: " + e.getMessage());
        }

        // Retorna a lista de autores (pode estar vazia se ocorrer algum erro).
        return lista;
    }

    // Método que atualiza os dados de um Autor existente na base de dados.
    public boolean atualizar(Autor autor) {
        // Query SQL para atualizar o nome e a nacionalidade do autor identificado pelo id.
        String sql = "UPDATE autor SET nome = ?, nacionalidade = ? WHERE id = ?";

        // Utiliza try-with-resources para gerir a ligação e o prepared statement.
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define os parâmetros da query com os novos valores do objeto Autor.
            stmt.setString(1, autor.getNome());
            stmt.setString(2, autor.getNacionalidade());
            stmt.setInt(3, autor.getId());

            // Executa a instrução de atualização na base de dados.
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            // Em caso de erro, regista a mensagem no console e retorna false.
            System.err.println("Erro ao atualizar autor: " + e.getMessage());
            return false;
        }
    }

    // Método que elimina um Autor da base de dados com base no seu identificador.
    public boolean excluir(int id) {
        // Query SQL para eliminar o autor cuja id coincide com a fornecida.
        String sql = "DELETE FROM autor WHERE id = ?";

        // Utiliza try-with-resources para gerir a ligação e o prepared statement.
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define o parâmetro id para a query.
            stmt.setInt(1, id);

            // Executa a instrução de eliminação na base de dados.
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            // Em caso de erro, regista a mensagem no console e retorna false.
            System.err.println("Erro ao excluir autor: " + e.getMessage());
            return false;
        }
    }

    // Método que procura e retorna um Autor com base no seu identificador.
    public Autor buscarPorId(int id) {
        // Query SQL para selecionar o autor com o id fornecido.
        String sql = "SELECT * FROM autor WHERE id = ?";
        Autor autor = null;

        // Utiliza try-with-resources para gerir a ligação e o prepared statement.
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define o parâmetro id para a query.
            stmt.setInt(1, id);

            // Executa a query e obtém o conjunto de resultados.
            ResultSet rs = stmt.executeQuery();

            // Se existir um registo, cria um objeto Autor com os dados obtidos.
            if (rs.next()) {
                autor = new Autor();
                autor.setId(rs.getInt("id"));
                autor.setNome(rs.getString("nome"));
                autor.setNacionalidade(rs.getString("nacionalidade"));
            }

        } catch (SQLException e) {
            // Em caso de erro, regista a mensagem no console.
            System.err.println("Erro ao buscar autor por ID: " + e.getMessage());
        }

        // Retorna o Autor encontrado ou null se não existir.
        return autor;
    }
}
