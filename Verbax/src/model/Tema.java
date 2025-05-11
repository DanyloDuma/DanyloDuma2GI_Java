// Classe que representa um Tema com atributos de identificação e nome.
package model;

public class Tema {
    // Variável privada para armazenar o identificador único do tema.
    private int id;
    // Variável privada para armazenar o nome do tema.
    private String nome;

    // Construtor por omissão que não inicializa os atributos.
    public Tema() {
    }

    // Construtor parametrizado que inicializa os atributos da classe.
    public Tema(int id, String nome) {
        this.id = id;       // Atribuição do identificador à variável de instância.
        this.nome = nome;   // Atribuição do nome à variável de instância.
    }

    // Getters e Setters

    // Método que devolve o identificador do tema.
    public int getId() {
        return id;
    }

    // Método que define um novo valor para o identificador do tema.
    public void setId(int id) {
        this.id = id;
    }

    // Método que devolve o nome do tema.
    public String getNome() {
        return nome;
    }

    // Método que define um novo valor para o nome do tema.
    public void setNome(String nome) {
        this.nome = nome;
    }
}
