package controller;

import dao.EditoraDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Editora;

public class EditoraController {

    @FXML private TextField txtEditoraId;
    @FXML private TextField txtEditoraNome;
    @FXML private TextField txtEditoraCidade;

    @FXML private TableView<Editora> tblEditoras;
    @FXML private TableColumn<Editora, Integer> colEditoraId;
    @FXML private TableColumn<Editora, String> colEditoraNome;
    @FXML private TableColumn<Editora, String> colEditoraCidade;

    private final EditoraDAO editoraDAO = new EditoraDAO();
    private final ObservableList<Editora> listaEditoras = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        colEditoraId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colEditoraNome.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNome()));
        colEditoraCidade.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCidade()));
        carregarEditoras();
    }

    private void carregarEditoras() {
        listaEditoras.setAll(editoraDAO.listarTodos());
        tblEditoras.setItems(listaEditoras);
    }

    @FXML
    private void handleSalvarEditora() {
        String nome = txtEditoraNome.getText();
        String cidade = txtEditoraCidade.getText();

        if (nome.isEmpty() || cidade.isEmpty()) {
            mostrarAlerta("Erro", "Nome e Cidade são obrigatórios.");
            return;
        }

        Editora editora = new Editora();
        editora.setNome(nome);
        editora.setCidade(cidade);

        if (editoraDAO.inserir(editora)) {
            mostrarAlerta("Sucesso", "Editora salva com sucesso.");
            limparCampos();
            carregarEditoras();
        } else {
            mostrarAlerta("Erro", "Erro ao salvar editora.");
        }
    }

    @FXML
    private void handleAtualizarEditora() {
        try {
            int id = Integer.parseInt(txtEditoraId.getText());
            String nome = txtEditoraNome.getText();
            String cidade = txtEditoraCidade.getText();

            Editora editora = new Editora(id, nome, cidade);
            if (editoraDAO.atualizar(editora)) {
                mostrarAlerta("Sucesso", "Editora atualizada com sucesso.");
                limparCampos();
                carregarEditoras();
            } else {
                mostrarAlerta("Erro", "Erro ao atualizar editora.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Erro", "ID inválido.");
        }
    }

    @FXML
    private void handleExcluirEditora() {
        try {
            int id = Integer.parseInt(txtEditoraId.getText());
            if (editoraDAO.excluir(id)) {
                mostrarAlerta("Sucesso", "Editora excluída com sucesso.");
                limparCampos();
                carregarEditoras();
            } else {
                mostrarAlerta("Erro", "Erro ao excluir editora.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Erro", "ID inválido.");
        }
    }

    @FXML
    private void handleBuscarEditoraPorId() {
        try {
            int id = Integer.parseInt(txtEditoraId.getText());
            Editora editora = editoraDAO.buscarPorId(id);
            if (editora != null) {
                txtEditoraNome.setText(editora.getNome());
                txtEditoraCidade.setText(editora.getCidade());
            } else {
                mostrarAlerta("Aviso", "Editora não encontrada.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Erro", "ID inválido.");
        }
    }

    @FXML
    private void handleLimparEditora() {
        limparCampos();
    }

    private void limparCampos() {
        txtEditoraId.clear();
        txtEditoraNome.clear();
        txtEditoraCidade.clear();
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
