package dao;

import model.*;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) responsável pelas operações de persistência da entidade Livro.
 * Contém métodos para inserir, listar, atualizar, excluir e buscar livros no banco de dados.
 */
public class LivroDAO {

    /**
     * Insere um novo livro no banco de dados.
     *
     * @param livro O objeto Livro a ser inserido.
     * @return true se a inserção for bem-sucedida, false caso contrário.
     */
    public boolean inserir(Livro livro) {
        String sql = "INSERT INTO livro (titulo, ano_publicacao, isbn, id_autor, id_tema, id_editora, id_localizacao) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, livro.getTitulo());
            stmt.setInt(2, livro.getAnoPublicacao());
            stmt.setString(3, livro.getIsbn());
            stmt.setInt(4, livro.getAutor().getId());
            stmt.setInt(5, livro.getTema().getId());
            stmt.setInt(6, livro.getEditora().getId());
            stmt.setInt(7, livro.getLocalizacao().getId());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao inserir livro: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lista todos os livros cadastrados no banco de dados, incluindo suas entidades relacionadas.
     *
     * @return Uma lista de objetos Livro.
     */
    public List<Livro> listarTodos() {
        List<Livro> lista = new ArrayList<>();
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

            while (rs.next()) {
                Livro livro = new Livro();
                livro.setId(rs.getInt("livro_id"));
                livro.setTitulo(rs.getString("titulo"));
                livro.setAnoPublicacao(rs.getInt("ano_publicacao"));
                livro.setIsbn(rs.getString("isbn"));

                Autor autor = new Autor();
                autor.setId(rs.getInt("autor_id"));
                autor.setNome(rs.getString("autor_nome"));
                autor.setNacionalidade(rs.getString("nacionalidade"));
                livro.setAutor(autor);

                Tema tema = new Tema();
                tema.setId(rs.getInt("tema_id"));
                tema.setNome(rs.getString("tema_nome"));
                livro.setTema(tema);

                Editora editora = new Editora();
                editora.setId(rs.getInt("editora_id"));
                editora.setNome(rs.getString("editora_nome"));
                editora.setCidade(rs.getString("cidade"));
                livro.setEditora(editora);

                Localizacao loc = new Localizacao();
                loc.setId(rs.getInt("localizacao_id"));
                loc.setSetor(rs.getString("setor"));
                loc.setPrateleira(rs.getString("prateleira"));
                livro.setLocalizacao(loc);

                lista.add(livro);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar livros: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Atualiza um livro existente no banco de dados.
     *
     * @param livro O objeto Livro com os dados atualizados.
     * @return true se a atualização for bem-sucedida, false caso contrário.
     */
    public boolean atualizar(Livro livro) {
        String sql = "UPDATE livro SET titulo = ?, ano_publicacao = ?, isbn = ?, id_autor = ?, id_tema = ?, id_editora = ?, id_localizacao = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, livro.getTitulo());
            stmt.setInt(2, livro.getAnoPublicacao());
            stmt.setString(3, livro.getIsbn());
            stmt.setInt(4, livro.getAutor().getId());
            stmt.setInt(5, livro.getTema().getId());
            stmt.setInt(6, livro.getEditora().getId());
            stmt.setInt(7, livro.getLocalizacao().getId());
            stmt.setInt(8, livro.getId());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar livro: " + e.getMessage());
            return false;
        }
    }

    /**
     * Exclui um livro do banco de dados com base no seu ID.
     *
     * @param id O ID do livro a ser excluído.
     * @return true se a exclusão for bem-sucedida, false caso contrário.
     */
    public boolean excluir(int id) {
        String sql = "DELETE FROM livro WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir livro: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca um livro pelo seu ID, retornando também as informações completas das entidades relacionadas.
     *
     * @param id O ID do livro a ser buscado.
     * @return Um objeto Livro, ou null se não encontrado ou em caso de erro.
     */
    public Livro buscarPorId(int id) {
        Livro livro = null;
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

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                livro = new Livro();
                livro.setId(rs.getInt("livro_id"));
                livro.setTitulo(rs.getString("titulo"));
                livro.setAnoPublicacao(rs.getInt("ano_publicacao"));
                livro.setIsbn(rs.getString("isbn"));

                Autor autor = new Autor();
                autor.setId(rs.getInt("autor_id"));
                autor.setNome(rs.getString("autor_nome"));
                autor.setNacionalidade(rs.getString("nacionalidade"));
                livro.setAutor(autor);

                Tema tema = new Tema();
                tema.setId(rs.getInt("tema_id"));
                tema.setNome(rs.getString("tema_nome"));
                livro.setTema(tema);

                Editora editora = new Editora();
                editora.setId(rs.getInt("editora_id"));
                editora.setNome(rs.getString("editora_nome"));
                editora.setCidade(rs.getString("cidade"));
                livro.setEditora(editora);

                Localizacao loc = new Localizacao();
                loc.setId(rs.getInt("localizacao_id"));
                loc.setSetor(rs.getString("setor"));
                loc.setPrateleira(rs.getString("prateleira"));
                livro.setLocalizacao(loc);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar livro: " + e.getMessage());
        }

        return livro;
    }
}
