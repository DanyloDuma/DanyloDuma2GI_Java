package dao;

import model.*;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {

    // Método para inserir um novo livro no banco de dados
    public boolean inserir(Livro livro) {
        // SQL para inserir um livro, referenciando os IDs das entidades relacionadas
        String sql = "INSERT INTO livro (titulo, ano_publicacao, isbn, id_autor, id_tema, id_editora, id_localizacao) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Definindo os parâmetros da query com base no objeto Livro
            stmt.setString(1, livro.getTitulo());
            stmt.setInt(2, livro.getAnoPublicacao());
            stmt.setString(3, livro.getIsbn());
            // Obtendo os IDs dos objetos relacionados
            stmt.setInt(4, livro.getAutor().getId());
            stmt.setInt(5, livro.getTema().getId());
            stmt.setInt(6, livro.getEditora().getId());
            stmt.setInt(7, livro.getLocalizacao().getId());

            // Executando a query de inserção
            stmt.executeUpdate();
            return true; // Retorna true se a inserção for bem-sucedida

        } catch (SQLException e) {
            // Em caso de erro, imprime a mensagem de erro e retorna false
            System.err.println("Erro ao inserir livro: " + e.getMessage());
            return false;
        }
    }

    // Método para listar todos os livros com informações completas das entidades relacionadas
    public List<Livro> listarTodos() {
        List<Livro> lista = new ArrayList<>();
        // SQL com JOINs para obter dados de livro, autor, tema, editora e localização
        String sql = "SELECT " +
                "l.id AS livro_id, l.titulo, l.ano_publicacao, l.isbn, " +
                "a.id AS autor_id, a.nome AS autor_nome, a.nacionalidade, " +
                "t.id AS tema_id, t.nome AS tema_nome, " +
                "e.id AS editora_id, e.nome AS editora_nome, e.cidade, " +
                "loc.id AS localizacao_id, loc.setor, loc.prateleira " +
                "FROM livro l " +
                "JOIN autor a ON l.id_autor = a.id " +
                "JOIN tema t ON l.id_tema = t.id " +
                "JOIN editora e ON l.id_editora = e.id " +
                "JOIN localizacao loc ON l.id_localizacao = loc.id";


        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Iterando sobre os resultados da query
            while (rs.next()) {
                // Criando um novo objeto Livro
                Livro livro = new Livro();
                // Populando os atributos do Livro
                livro.setId(rs.getInt("livro_id"));
                livro.setTitulo(rs.getString("titulo"));
                livro.setAnoPublicacao(rs.getInt("ano_publicacao"));
                livro.setIsbn(rs.getString("isbn"));

                // Criando e populando o objeto Autor associado
                Autor autor = new Autor();
                autor.setId(rs.getInt("autor_id"));
                autor.setNome(rs.getString("autor_nome"));
                autor.setNacionalidade(rs.getString("nacionalidade"));
                livro.setAutor(autor);

                // Criando e populando o objeto Tema associado
                Tema tema = new Tema();
                tema.setId(rs.getInt("tema_id"));
                tema.setNome(rs.getString("tema_nome"));
                livro.setTema(tema);

                // Criando e populando o objeto Editora associada
                Editora editora = new Editora();
                editora.setId(rs.getInt("editora_id"));
                editora.setNome(rs.getString("editora_nome"));
                editora.setCidade(rs.getString("cidade"));
                livro.setEditora(editora);

                // Criando e populando o objeto Localizacao associada
                Localizacao loc = new Localizacao();
                loc.setId(rs.getInt("localizacao_id"));
                loc.setSetor(rs.getString("setor"));
                loc.setPrateleira(rs.getString("prateleira"));
                livro.setLocalizacao(loc);

                // Adicionando o livro à lista
                lista.add(livro);
            }

        } catch (SQLException e) {
            // Em caso de erro, imprime a mensagem de erro
            System.err.println("Erro ao listar livros: " + e.getMessage());
        }

        return lista; // Retorna a lista de livros (pode estar vazia em caso de erro ou sem resultados)
    }

    // Método para atualizar um livro existente no banco de dados
    public boolean atualizar(Livro livro) {
        // SQL para atualizar um livro, referenciando os IDs das entidades relacionadas
        String sql = "UPDATE livro SET titulo = ?, ano_publicacao = ?, isbn = ?, id_autor = ?, id_tema = ?, id_editora = ?, id_localizacao = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Definindo os parâmetros da query com base no objeto Livro
            stmt.setString(1, livro.getTitulo());
            stmt.setInt(2, livro.getAnoPublicacao());
            stmt.setString(3, livro.getIsbn());
            // Obtendo os IDs dos objetos relacionados
            stmt.setInt(4, livro.getAutor().getId());
            stmt.setInt(5, livro.getTema().getId());
            stmt.setInt(6, livro.getEditora().getId());
            stmt.setInt(7, livro.getLocalizacao().getId());
            stmt.setInt(8, livro.getId()); // ID do livro a ser atualizado

            // Executando a query de atualização
            stmt.executeUpdate();
            return true; // Retorna true se a atualização for bem-sucedida

        } catch (SQLException e) {
            // Em caso de erro, imprime a mensagem de erro e retorna false
            System.err.println("Erro ao atualizar livro: " + e.getMessage());
            return false;
        }
    }

    // Método para excluir um livro do banco de dados por ID
    public boolean excluir(int id) {
        // SQL para excluir um livro pelo seu ID
        String sql = "DELETE FROM livro WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Definindo o parâmetro da query (o ID do livro)
            stmt.setInt(1, id);
            // Executando a query de exclusão
            stmt.executeUpdate();
            return true; // Retorna true se a exclusão for bem-sucedida

        } catch (SQLException e) {
            // Em caso de erro, imprime a mensagem de erro e retorna false
            System.err.println("Erro ao excluir livro: " + e.getMessage());
            return false;
        }
    }

    // Método para buscar um livro por ID com informações completas das entidades relacionadas
    public Livro buscarPorId(int id) {
        Livro livro = null;
        // SQL com JOINs para obter dados de livro, autor, tema, editora e localização para um ID específico
        String sql = "SELECT " +
                "l.id AS livro_id, l.titulo, l.ano_publicacao, l.isbn, " +
                "a.id AS autor_id, a.nome AS autor_nome, a.nacionalidade, " +
                "t.id AS tema_id, t.nome AS tema_nome, " +
                "e.id AS editora_id, e.nome AS editora_nome, e.cidade, " +
                "loc.id AS localizacao_id, loc.setor, loc.prateleira " +
                "FROM livro l " +
                "JOIN autor a ON l.id_autor = a.id " +
                "JOIN tema t ON l.id_tema = t.id " +
                "JOIN editora e ON l.id_editora = e.id " +
                "JOIN localizacao loc ON l.id_localizacao = loc.id " +
                "WHERE l.id = ?";


        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Definindo o parâmetro da query (o ID do livro)
            stmt.setInt(1, id);
            // Executando a query de busca
            ResultSet rs = stmt.executeQuery();

            // Verificando se há resultados
            if (rs.next()) {
                // Criando um novo objeto Livro
                livro = new Livro();
                // Populando os atributos do Livro
                livro.setId(rs.getInt("livro_id"));
                livro.setTitulo(rs.getString("titulo"));
                livro.setAnoPublicacao(rs.getInt("ano_publicacao"));
                livro.setIsbn(rs.getString("isbn"));

                // Criando e populando o objeto Autor associado
                Autor autor = new Autor();
                autor.setId(rs.getInt("autor_id"));
                autor.setNome(rs.getString("autor_nome"));
                autor.setNacionalidade(rs.getString("nacionalidade"));
                livro.setAutor(autor);

                // Criando e populando o objeto Tema associado
                Tema tema = new Tema();
                tema.setId(rs.getInt("tema_id"));
                tema.setNome(rs.getString("tema_nome"));
                livro.setTema(tema);

                // Criando e populando o objeto Editora associada
                Editora editora = new Editora();
                editora.setId(rs.getInt("editora_id"));
                editora.setNome(rs.getString("editora_nome"));
                editora.setCidade(rs.getString("cidade"));
                livro.setEditora(editora);

                // Criando e populando o objeto Localizacao associada
                Localizacao loc = new Localizacao();
                loc.setId(rs.getInt("localizacao_id"));
                loc.setSetor(rs.getString("setor"));
                loc.setPrateleira(rs.getString("prateleira"));
                livro.setLocalizacao(loc);
            }

        } catch (SQLException e) {
            // Em caso de erro, imprime a mensagem de erro
            System.err.println("Erro ao buscar livro: " + e.getMessage());
        }

        return livro; // Retorna o livro encontrado (ou null se não encontrado ou em caso de erro)
    }
}