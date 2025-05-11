// Classe que representa uma Editora com os atributos de identificação, nome e cidade.
package model;

public class Editora {
    // Variável privada para armazenar o identificador único da editora.
    private int id;
    // Variável privada para armazenar o nome da editora.
    private String nome;
    // Variável privada para armazenar a cidade onde a editora se encontra.
    private String cidade;

    // Construtor por omissão que não inicializa os atributos.
    public Editora() {
    }

    // Construtor parametrizado que inicializa todos os atributos da classe.
    public Editora(int id, String nome, String cidade) {
        this.id = id;          // Atribuição do id à variável da classe.
        this.nome = nome;      // Atribuição do nome à variável da classe.
        this.cidade = cidade;  // Atribuição da cidade à variável da classe.
    }

    // Getters e Setters

    // Método que devolve o id da editora.
    public int getId() {
        return id;
    }

    // Método que define um novo valor para o id da editora.
    public void setId(int id) {
        this.id = id;
    }

    // Método que devolve o nome da editora.
    public String getNome() {
        return nome;
    }

    // Método que define um novo valor para o nome da editora.
    public void setNome(String nome) {
        this.nome = nome;
    }

    // Método que devolve a cidade onde a editora se encontra.
    public String getCidade() {
        return cidade;
    }

    // Método que define um novo valor para a cidade da editora.
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
}
