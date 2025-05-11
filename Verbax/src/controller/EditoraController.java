package controller;

import dao.EditoraDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
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
        configurarColunasTabela();
        carregarEditoras();

        tblEditoras.setOnMouseClicked(this::selecionarEditoraDaTabela);
    }

    private void configurarColunasTabela() {
        colEditoraId.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getId()).asObject());
        colEditoraNome.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getNome()));
        colEditoraCidade.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getCidade()));
    }

    private void carregarEditoras() {
        listaEditoras.setAll(editoraDAO.listarTodos());
        tblEditoras.setItems(listaEditoras);
    }

    @FXML
    private void handleSalvarEditora() {
        if (!validarCamposObrigatorios()) return;

        Editora novaEditora = new Editora(0, txtEditoraNome.getText(), txtEditoraCidade.getText());

        if (editoraDAO.inserir(novaEditora)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Editora salva com sucesso.");
            limparCampos();
            carregarEditoras();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao salvar editora.");
        }
    }

    @FXML
    private void handleAtualizarEditora() {
        if (!validarCamposObrigatorios() || txtEditoraId.getText().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Aviso", "Informe o ID da editora para atualizar.");
            return;
        }

        try {
            int id = Integer.parseInt(txtEditoraId.getText());
            Editora editora = new Editora(id, txtEditoraNome.getText(), txtEditoraCidade.getText());

            if (editoraDAO.atualizar(editora)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Editora atualizada com sucesso.");
                limparCampos();
                carregarEditoras();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao atualizar editora.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "ID inválido.");
        }
    }

    @FXML
    private void handleExcluirEditora() {
        try {
            int id = Integer.parseInt(txtEditoraId.getText());

            Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION, "Tem certeza que deseja excluir esta editora?", ButtonType.YES, ButtonType.NO);
            confirmacao.setHeaderText("Confirmação");
            confirmacao.showAndWait();

            if (confirmacao.getResult() == ButtonType.YES) {
                if (editoraDAO.excluir(id)) {
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Editora excluída com sucesso.");
                    limparCampos();
                    carregarEditoras();
                } else {
                    mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao excluir editora.");
                }
            }

        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "ID inválido.");
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
                mostrarAlerta(Alert.AlertType.WARNING, "Aviso", "Editora não encontrada.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "ID inválido.");
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
        tblEditoras.getSelectionModel().clearSelection();
    }

    private boolean validarCamposObrigatorios() {
        String nome = txtEditoraNome.getText();
        String cidade = txtEditoraCidade.getText();

        if (nome.isEmpty() || cidade.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Aviso", "Nome e Cidade são obrigatórios.");
            return false;
        }

        return true;
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    private void selecionarEditoraDaTabela(MouseEvent event) {
        Editora selecionada = tblEditoras.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            txtEditoraId.setText(String.valueOf(selecionada.getId()));
            txtEditoraNome.setText(selecionada.getNome());
            txtEditoraCidade.setText(selecionada.getCidade());
        }
    }
}
