package controller;

import dao.AutorDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Autor;

public class AutorController {

    @FXML private TextField txtAutorId;
    @FXML private TextField txtAutorNome;
    @FXML private TextField txtAutorNacionalidade;
    @FXML private TableView<Autor> tblAutores;
    @FXML private TableColumn<Autor, Integer> colAutorId;
    @FXML private TableColumn<Autor, String> colAutorNome;
    @FXML private TableColumn<Autor, String> colAutorNacionalidade;

    private final AutorDAO autorDAO = new AutorDAO();
    private final ObservableList<Autor> listaAutores = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        colAutorId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colAutorNome.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNome()));
        colAutorNacionalidade.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNacionalidade()));
        carregarAutores();
    }

    private void carregarAutores() {
        listaAutores.setAll(autorDAO.listarTodos());
        tblAutores.setItems(listaAutores);
    }

    @FXML
    private void handleSalvarAutor() {
        String nome = txtAutorNome.getText();
        String nacionalidade = txtAutorNacionalidade.getText();

        if (nome.isEmpty() || nacionalidade.isEmpty()) {
            mostrarAlerta("Erro", "Nome e Nacionalidade são obrigatórios.");
            return;
        }

        Autor autor = new Autor();
        autor.setNome(nome);
        autor.setNacionalidade(nacionalidade);

        if (autorDAO.inserir(autor)) {
            mostrarAlerta("Sucesso", "Autor salvo com sucesso.");
            limparCampos();
            carregarAutores();
        } else {
            mostrarAlerta("Erro", "Erro ao salvar autor.");
        }
    }

    @FXML
    private void handleAtualizarAutor() {
        try {
            int id = Integer.parseInt(txtAutorId.getText());
            String nome = txtAutorNome.getText();
            String nacionalidade = txtAutorNacionalidade.getText();

            Autor autor = new Autor(id, nome, nacionalidade);
            if (autorDAO.atualizar(autor)) {
                mostrarAlerta("Sucesso", "Autor atualizado com sucesso.");
                limparCampos();
                carregarAutores();
            } else {
                mostrarAlerta("Erro", "Erro ao atualizar autor.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Erro", "ID inválido.");
        }
    }

    @FXML
    private void handleExcluirAutor() {
        try {
            int id = Integer.parseInt(txtAutorId.getText());
            if (autorDAO.excluir(id)) {
                mostrarAlerta("Sucesso", "Autor excluído com sucesso.");
                limparCampos();
                carregarAutores();
            } else {
                mostrarAlerta("Erro", "Erro ao excluir autor.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Erro", "ID inválido.");
        }
    }

    @FXML
    private void handleBuscarAutorPorId() {
        try {
            int id = Integer.parseInt(txtAutorId.getText());
            Autor autor = autorDAO.buscarPorId(id);
            if (autor != null) {
                txtAutorNome.setText(autor.getNome());
                txtAutorNacionalidade.setText(autor.getNacionalidade());
            } else {
                mostrarAlerta("Aviso", "Autor não encontrado.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Erro", "ID inválido.");
        }
    }

    @FXML
    private void handleLimparAutor() {
        limparCampos();
    }

    private void limparCampos() {
        txtAutorId.clear();
        txtAutorNome.clear();
        txtAutorNacionalidade.clear();
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
