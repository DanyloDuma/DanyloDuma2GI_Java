// Classe que representa um Autor com atributos de identificação, nome e nacionalidade.
package model;

public class Autor {
    // Variável privada para armazenar o identificador único do autor.
    private int id;
    // Variável privada para armazenar o nome do autor.
    private String nome;
    // Variável privada para armazenar a nacionalidade do autor.
    private String nacionalidade;

    // Construtor por omissão que não inicializa os atributos.
    public Autor() {
    }

    // Construtor parametrizado que inicializa todos os atributos da classe.
    public Autor(int id, String nome, String nacionalidade) {
        this.id = id;                     // Atribuição do id à variável da classe.
        this.nome = nome;                 // Atribuição do nome à variável da classe.
        this.nacionalidade = nacionalidade; // Atribuição da nacionalidade à variável da classe.
    }

    // Getters e Setters

    // Método que devolve o id do autor.
    public int getId() {
        return id;
    }

    // Método que define um novo valor para o id do autor.
    public void setId(int id) {
        this.id = id;
    }

    // Método que devolve o nome do autor.
    public String getNome() {
        return nome;
    }

    // Método que define um novo valor para o nome do autor.
    public void setNome(String nome) {
        this.nome = nome;
    }

    // Método que devolve a nacionalidade do autor.
    public String getNacionalidade() {
        return nacionalidade;
    }

    // Método que define um novo valor para a nacionalidade do autor.
    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }
}
