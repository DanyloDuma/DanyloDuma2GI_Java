// Classe que representa uma Localização com atributos de identificação, setor e prateleira.
package model;

public class Localizacao {
    // Variável privada que guarda o identificador único da localização.
    private int id;
    // Variável privada que guarda o setor da localização.
    private String setor;
    // Variável privada que guarda a prateleira da localização.
    private String prateleira;

    // Construtor por omissão que não inicializa os atributos.
    public Localizacao() {
    }

    // Construtor parametrizado que inicializa todos os atributos da classe.
    public Localizacao(int id, String setor, String prateleira) {
        this.id = id;             // Atribui o valor do parâmetro 'id' à variável de instância 'id'.
        this.setor = setor;       // Atribui o valor do parâmetro 'setor' à variável de instância 'setor'.
        this.prateleira = prateleira; // Atribui o valor do parâmetro 'prateleira' à variável de instância 'prateleira'.
    }

    // Getters e Setters

    // Método que devolve o identificador da localização.
    public int getId() {
        return id;
    }

    // Método que define um novo valor para o identificador da localização.
    public void setId(int id) {
        this.id = id;
    }

    // Método que devolve o setor da localização.
    public String getSetor() {
        return setor;
    }

    // Método que define um novo valor para o setor da localização.
    public void setSetor(String setor) {
        this.setor = setor;
    }

    // Método que devolve a prateleira da localização.
    public String getPrateleira() {
        return prateleira;
    }

    // Método que define um novo valor para a prateleira da localização.
    public void setPrateleira(String prateleira) {
        this.prateleira = prateleira;
    }
}
