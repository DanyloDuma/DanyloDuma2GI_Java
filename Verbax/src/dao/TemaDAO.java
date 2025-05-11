// Classe de acesso a dados (DAO) para a entidade Tema.
// Responsável pelos métodos CRUD relativos à tabela "tema" na base de dados.
package dao;

import model.Tema;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TemaDAO {

    // Método que insere um novo Tema na base de dados.
    public boolean inserir(Tema tema) {
        // Query SQL para inserir o nome do tema.
        String sql = "INSERT INTO tema (nome) VALUES (?)";

        // Utiliza try-with-resources para garantir o fecho automático dos recursos utilizados.
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define o valor do parâmetro na query com o nome do tema.
            stmt.setString(1, tema.getNome());
            // Executa a query de inserção.
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            // Em caso de erro, regista a mensagem de erro e retorna false.
            System.err.println("Erro ao inserir tema: " + e.getMessage());
            return false;
        }
    }

    // Método que retorna uma lista com todos os Temas registados na base de dados.
    public List<Tema> listarTodos() {
        // Cria uma lista para armazenar os objetos Tema.
        List<Tema> lista = new ArrayList<>();
        // Query SQL para selecionar todos os registos da tabela "tema".
        String sql = "SELECT * FROM tema";

        // Utiliza try-with-resources para gerir automaticamente a ligação, o statement e o result set.
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Itera por cada registo retornado no result set.
            while (rs.next()) {
                // Cria um novo objeto Tema e define os seus atributos com os dados do registo.
                Tema tema = new Tema();
                tema.setId(rs.getInt("id"));
                tema.setNome(rs.getString("nome"));
                // Adiciona o objeto à lista.
                lista.add(tema);
            }

        } catch (SQLException e) {
            // Em caso de exceção, regista a mensagem de erro.
            System.err.println("Erro ao listar temas: " + e.getMessage());
        }

        // Retorna a lista de Temas (pode estar vazia se ocorrer um erro).
        return lista;
    }

    // Método que atualiza os dados de um Tema existente na base de dados.
    public boolean atualizar(Tema tema) {
        // Query SQL para atualizar o nome do tema identificado pelo id.
        String sql = "UPDATE tema SET nome = ? WHERE id = ?";

        // Utiliza try-with-resources para gerir a ligação e o prepared statement.
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define os parâmetros da query: novo nome e o id do tema a atualizar.
            stmt.setString(1, tema.getNome());
            stmt.setInt(2, tema.getId());
            // Executa a query de atualização.
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            // Em caso de erro, regista a mensagem de erro e retorna false.
            System.err.println("Erro ao atualizar tema: " + e.getMessage());
            return false;
        }
    }

    // Método que elimina um Tema da base de dados com base no seu identificador.
    public boolean excluir(int id) {
        // Query SQL para eliminar o tema cujo id corresponda ao fornecido.
        String sql = "DELETE FROM tema WHERE id = ?";

        // Utiliza try-with-resources para gerir automaticamente a ligação e o prepared statement.
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define o parâmetro da query com o id do tema a eliminar.
            stmt.setInt(1, id);
            // Executa a query de eliminação.
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            // Em caso de exceção, regista a mensagem de erro e retorna false.
            System.err.println("Erro ao excluir tema: " + e.getMessage());
            return false;
        }
    }

    // Método que procura e retorna um Tema a partir do seu identificador.
    public Tema buscarPorId(int id) {
        // Query SQL para selecionar o tema com o id indicado.
        String sql = "SELECT * FROM tema WHERE id = ?";
        // Inicializa a variável Tema como null.
        Tema tema = null;

        // Utiliza try-with-resources para gerir a ligação e o prepared statement.
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define o parâmetro da query com o id do tema a procurar.
            stmt.setInt(1, id);
            // Executa a query e obtém o result set.
            ResultSet rs = stmt.executeQuery();

            // Se um registo for encontrado, cria um objeto Tema e define os seus atributos.
            if (rs.next()) {
                tema = new Tema();
                tema.setId(rs.getInt("id"));
                tema.setNome(rs.getString("nome"));
            }

        } catch (SQLException e) {
            // Em caso de erro, regista a mensagem de erro.
            System.err.println("Erro ao buscar tema: " + e.getMessage());
        }

        // Retorna o objeto Tema encontrado ou null se não for encontrado.
        return tema;
    }
}
