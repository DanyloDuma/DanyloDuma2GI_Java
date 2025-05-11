package controller;

import dao.AutorDAO;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Autor;

import java.util.logging.Level;
import java.util.logging.Logger;

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
    private static final Logger logger = Logger.getLogger(AutorController.class.getName());

    @FXML
    private void initialize() {
        colAutorId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colAutorNome.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));
        colAutorNacionalidade.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNacionalidade()));

        tblAutores.setItems(listaAutores);
        carregarAutores();
        configurarSelecaoTabela();
        txtAutorId.setDisable(true); // ID só é preenchido ao selecionar ou buscar
    }

    private void carregarAutores() {
        listaAutores.setAll(autorDAO.listarTodos());
    }

    private void configurarSelecaoTabela() {
        tblAutores.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                txtAutorId.setText(String.valueOf(newSel.getId()));
                txtAutorNome.setText(newSel.getNome());
                txtAutorNacionalidade.setText(newSel.getNacionalidade());
            }
        });
    }

    @FXML
    private void handleSalvarAutor() {
        String nome = txtAutorNome.getText().trim();
        String nacionalidade = txtAutorNacionalidade.getText().trim();

        if (nome.isEmpty() || nacionalidade.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Erro de Validação", "Nome e Nacionalidade são obrigatórios.");
            return;
        }

        Autor autor = new Autor();
        autor.setNome(nome);
        autor.setNacionalidade(nacionalidade);

        if (autorDAO.inserir(autor)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Autor salvo com sucesso.");
            limparCampos();
            carregarAutores();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao salvar autor.");
        }
    }

    @FXML
    private void handleAtualizarAutor() {
        String idStr = txtAutorId.getText().trim();
        String nome = txtAutorNome.getText().trim();
        String nacionalidade = txtAutorNacionalidade.getText().trim();

        if (idStr.isEmpty() || nome.isEmpty() || nacionalidade.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Erro de Validação", "Todos os campos devem ser preenchidos para atualização.");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            Autor autor = new Autor(id, nome, nacionalidade);

            if (autorDAO.atualizar(autor)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Autor atualizado com sucesso.");
                limparCampos();
                carregarAutores();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao atualizar autor.");
            }
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "ID inválido para atualização: " + idStr, e);
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "ID inválido para atualização.");
        }
    }

    @FXML
    private void handleExcluirAutor() {
        String idStr = txtAutorId.getText().trim();

        if (idStr.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Erro de Validação", "Informe o ID do autor a ser excluído.");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);

            if (autorDAO.excluir(id)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Autor excluído com sucesso.");
                limparCampos();
                carregarAutores();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao excluir autor.");
            }
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "ID inválido para exclusão: " + idStr, e);
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "ID inválido para exclusão.");
        }
    }

    @FXML
    private void handleBuscarAutorPorId() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Buscar Autor");
        dialog.setHeaderText("Buscar autor por ID");
        dialog.setContentText("Informe o ID do autor:");

        dialog.showAndWait().ifPresent(idStr -> {
            try {
                int id = Integer.parseInt(idStr);
                Autor autor = autorDAO.buscarPorId(id);
                if (autor != null) {
                    txtAutorId.setText(String.valueOf(autor.getId()));
                    txtAutorNome.setText(autor.getNome());
                    txtAutorNacionalidade.setText(autor.getNacionalidade());
                } else {
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Resultado", "Autor não encontrado.");
                }
            } catch (NumberFormatException e) {
                logger.log(Level.WARNING, "ID inválido na busca: " + idStr, e);
                mostrarAlerta(Alert.AlertType.ERROR, "Erro", "ID inválido.");
            }
        });
    }

    @FXML
    private void handleLimparAutor() {
        limparCampos();
        tblAutores.getSelectionModel().clearSelection();
    }

    private void limparCampos() {
        txtAutorId.clear();
        txtAutorNome.clear();
        txtAutorNacionalidade.clear();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
