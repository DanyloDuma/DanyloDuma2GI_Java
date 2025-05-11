// Classe que representa um Livro com as suas propriedades e associações a outras entidades (Autor, Tema, Editora e Localização).
package model;

public class Livro {
    // Variável privada para armazenar o identificador único do livro.
    private int id;
    // Variável privada para armazenar o título do livro.
    private String titulo;
    // Variável privada para armazenar o ano de publicação do livro.
    private int anoPublicacao;
    // Variável privada para armazenar o ISBN do livro.
    private String isbn;

    // Variável privada para armazenar a instância do Autor associado ao livro.
    private Autor autor;
    // Variável privada para armazenar a instância do Tema associado ao livro.
    private Tema tema;
    // Variável privada para armazenar a instância da Editora associada ao livro.
    private Editora editora;
    // Variável privada para armazenar a instância da Localização associada ao livro.
    private Localizacao localizacao;

    // Construtor por omissão que não inicializa os atributos.
    public Livro() {
    }

    // Construtor parametrizado que inicializa todos os atributos da classe.
    public Livro(int id, String titulo, int anoPublicacao, String isbn,
                 Autor autor, Tema tema, Editora editora, Localizacao localizacao) {
        this.id = id;                         // Atribuição do identificador do livro.
        this.titulo = titulo;                 // Atribuição do título do livro.
        this.anoPublicacao = anoPublicacao;    // Atribuição do ano de publicação do livro.
        this.isbn = isbn;                     // Atribuição do ISBN do livro.
        this.autor = autor;                   // Atribuição do autor associado ao livro.
        this.tema = tema;                     // Atribuição do tema associado ao livro.
        this.editora = editora;               // Atribuição da editora associada ao livro.
        this.localizacao = localizacao;       // Atribuição da localização associada ao livro.
    }

    // Getters e Setters

    // Método que devolve o identificador do livro.
    public int getId() {
        return id;
    }

    // Método que define um novo valor para o identificador do livro.
    public void setId(int id) {
        this.id = id;
    }

    // Método que devolve o título do livro.
    public String getTitulo() {
        return titulo;
    }

    // Método que define um novo valor para o título do livro.
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    // Método que devolve o ano de publicação do livro.
    public int getAnoPublicacao() {
        return anoPublicacao;
    }

    // Método que define um novo valor para o ano de publicação do livro.
    public void setAnoPublicacao(int anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    // Método que devolve o ISBN do livro.
    public String getIsbn() {
        return isbn;
    }

    // Método que define um novo valor para o ISBN do livro.
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    // Método que devolve o objeto Autor associado ao livro.
    public Autor getAutor() {
        return autor;
    }

    // Método que define um novo objeto Autor para o livro.
    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    // Método que devolve o objeto Tema associado ao livro.
    public Tema getTema() {
        return tema;
    }

    // Método que define um novo objeto Tema para o livro.
    public void setTema(Tema tema) {
        this.tema = tema;
    }

    // Método que devolve o objeto Editora associado ao livro.
    public Editora getEditora() {
        return editora;
    }

    // Método que define um novo objeto Editora para o livro.
    public void setEditora(Editora editora) {
        this.editora = editora;
    }

    // Método que devolve o objeto Localizacao associado ao livro.
    public Localizacao getLocalizacao() {
        return localizacao;
    }

    // Método que define um novo objeto Localizacao para o livro.
    public void setLocalizacao(Localizacao localizacao) {
        this.localizacao = localizacao;
    }
}
