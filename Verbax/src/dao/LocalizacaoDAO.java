// Classe de acesso a dados (DAO) para a entidade Localizacao.
// Responsável pelas operações CRUD (inserir, listar, atualizar, excluir e buscar por ID) na tabela "localizacao" da base de dados.
package dao;

import model.Localizacao;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocalizacaoDAO {

    // Método que insere uma nova Localizacao na base de dados.
    public boolean inserir(Localizacao loc) {
        // Declaração da query SQL para inserir os dados de setor e prateleira.
        String sql = "INSERT INTO localizacao (setor, prateleira) VALUES (?, ?)";

        // try-with-resources garante que a ligação e o prepared statement sejam fechados automaticamente.
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define os parâmetros da query com os valores do objeto Localizacao.
            stmt.setString(1, loc.getSetor());
            stmt.setString(2, loc.getPrateleira());
            // Executa a query de inserção.
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            // Em caso de erro, exibe a mensagem de erro no console e retorna false.
            System.err.println("Erro ao inserir localizacao: " + e.getMessage());
            return false;
        }
    }

    // Método que retorna uma lista com todas as Localizacoes registadas na base de dados.
    public List<Localizacao> listarTodos() {
        // Declaração de uma lista para armazenar os registos de Localizacao.
        List<Localizacao> lista = new ArrayList<>();
        // Query SQL para selecionar todos os registos da tabela "localizacao".
        String sql = "SELECT * FROM localizacao";

        // try-with-resources para gerir automaticamente a ligação, statement e result set.
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Itera sobre cada registo retornado pelo result set.
            while (rs.next()) {
                // Cria uma nova instância de Localizacao e define os seus atributos com os dados da query.
                Localizacao loc = new Localizacao();
                loc.setId(rs.getInt("id"));
                loc.setSetor(rs.getString("setor"));
                loc.setPrateleira(rs.getString("prateleira"));
                // Adiciona o objeto Localizacao à lista.
                lista.add(loc);
            }

        } catch (SQLException e) {
            // Caso ocorra algum erro, exibe a mensagem de erro no console.
            System.err.println("Erro ao listar localizacoes: " + e.getMessage());
        }

        // Retorna a lista de Localizacoes (pode estar vazia se ocorrer um erro).
        return lista;
    }

    // Método que atualiza os dados de uma Localizacao existente na base de dados.
    public boolean atualizar(Localizacao loc) {
        // Query SQL para atualizar os campos setor e prateleira para um dado id.
        String sql = "UPDATE localizacao SET setor = ?, prateleira = ? WHERE id = ?";

        // Utiliza try-with-resources para gerir a ligação e o prepared statement.
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define os parâmetros da query com os novos valores do objeto Localizacao.
            stmt.setString(1, loc.getSetor());
            stmt.setString(2, loc.getPrateleira());
            stmt.setInt(3, loc.getId());
            // Executa a query de atualização.
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            // Em caso de erro, regista a mensagem de erro e retorna false.
            System.err.println("Erro ao atualizar localizacao: " + e.getMessage());
            return false;
        }
    }

    // Método que elimina uma Localizacao da base de dados com base no seu identificador.
    public boolean excluir(int id) {
        // Query SQL para eliminar o registo cujo id corresponda ao fornecido.
        String sql = "DELETE FROM localizacao WHERE id = ?";

        // try-with-resources para gerir a ligação e o prepared statement.
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define o parâmetro para a query.
            stmt.setInt(1, id);
            // Executa a query de eliminação.
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            // Se ocorrer um erro, exibe a mensagem de erro no console e retorna false.
            System.err.println("Erro ao excluir localizacao: " + e.getMessage());
            return false;
        }
    }

    // Método que procura e retorna uma Localizacao através do seu identificador.
    public Localizacao buscarPorId(int id) {
        // Query SQL para selecionar o registo da Localizacao com o id especificado.
        String sql = "SELECT * FROM localizacao WHERE id = ?";
        // Inicialmente, a variável local deste método é null.
        Localizacao loc = null;

        // Utiliza try-with-resources para gerir a ligação e o prepared statement.
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define o parâmetro id para a query.
            stmt.setInt(1, id);
            // Executa a query e armazena o resultado num ResultSet.
            ResultSet rs = stmt.executeQuery();

            // Se um registo existir, cria uma instância de Localizacao e define os seus atributos.
            if (rs.next()) {
                loc = new Localizacao();
                loc.setId(rs.getInt("id"));
                loc.setSetor(rs.getString("setor"));
                loc.setPrateleira(rs.getString("prateleira"));
            }

        } catch (SQLException e) {
            // Caso ocorra um erro, exibe a mensagem de erro no console.
            System.err.println("Erro ao buscar localizacao por ID: " + e.getMessage());
        }

        // Retorna a Localizacao encontrada ou null, caso não exista.
        return loc;
    }
}
