package model;

public class Localizacao {
    private int id;
    private String setor;
    private String prateleira;

    public Localizacao() {}

    public Localizacao(int id, String setor, String prateleira) {
        this.id = id;
        this.setor = setor;
        this.prateleira = prateleira;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getSetor() { return setor; }
    public void setSetor(String setor) { this.setor = setor; }

    public String getPrateleira() { return prateleira; }
    public void setPrateleira(String prateleira) { this.prateleira = prateleira; }
}
