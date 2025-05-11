package controller;

import dao.TemaDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Tema;

import java.util.List;
import java.util.Optional;

public class TemaController {

    @FXML private TextField txtTemaId;
    @FXML private TextField txtTemaNome;
    @FXML private Button btnTemaSalvar, btnTemaAtualizar, btnTemaExcluir, btnTemaLimpar, btnTemaBuscar;
    @FXML private TableView<Tema> tblTemas;
    @FXML private TableColumn<Tema, Integer> colTemaId;
    @FXML private TableColumn<Tema, String> colTemaNome;

    private final TemaDAO temaDAO = new TemaDAO();
    private final ObservableList<Tema> temaData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configurarTabela();
        configurarEventosTabela();
        carregarTemas();
        estadoInicial();
    }

    private void configurarTabela() {
        colTemaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTemaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tblTemas.setItems(temaData);
    }

    private void configurarEventosTabela() {
        tblTemas.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> preencherCampos(newVal)
        );
    }

    private void carregarTemas() {
        temaData.setAll(temaDAO.listarTodos());
    }

    private void preencherCampos(Tema tema) {
        if (tema != null) {
            txtTemaId.setText(String.valueOf(tema.getId()));
            txtTemaNome.setText(tema.getNome());
            txtTemaId.setDisable(true);
            btnTemaSalvar.setDisable(true);
            btnTemaAtualizar.setDisable(false);
            btnTemaExcluir.setDisable(false);
        }
    }

    @FXML
    private void handleSalvarTema() {
        String nome = txtTemaNome.getText().trim();

        if (nome.isEmpty()) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro de Validação", "O campo Nome é obrigatório.");
            return;
        }

        Tema novoTema = new Tema(0, nome);
        if (temaDAO.inserir(novoTema)) {
            sucesso("Tema salvo com sucesso!");
        } else {
            erro("Falha ao salvar o tema.");
        }
    }

    @FXML
    private void handleAtualizarTema() {
        try {
            int id = Integer.parseInt(txtTemaId.getText().trim());
            String nome = txtTemaNome.getText().trim();

            if (nome.isEmpty()) {
                exibirAlerta(Alert.AlertType.ERROR, "Erro de Validação", "O campo Nome é obrigatório.");
                return;
            }

            Tema tema = new Tema(id, nome);
            if (temaDAO.atualizar(tema)) {
                sucesso("Tema atualizado com sucesso!");
            } else {
                erro("Falha ao atualizar o tema.");
            }
        } catch (NumberFormatException e) {
            erro("ID inválido.");
        }
    }

    @FXML
    private void handleExcluirTema() {
        try {
            int id = Integer.parseInt(txtTemaId.getText().trim());

            if (!confirmar("Tem certeza que deseja excluir o tema com ID " + id + "?")) return;

            if (temaDAO.excluir(id)) {
                sucesso("Tema excluído com sucesso!");
            } else {
                erro("Falha ao excluir o tema. Verifique se o ID existe ou está em uso.");
            }

        } catch (NumberFormatException e) {
            erro("ID inválido.");
        }
    }

    @FXML
    private void handleLimparTema() {
        txtTemaId.clear();
        txtTemaNome.clear();
        txtTemaId.setDisable(false);
        tblTemas.getSelectionModel().clearSelection();
        estadoInicial();
    }

    @FXML
    private void handleBuscarTemaPorId() {
        try {
            int id = Integer.parseInt(txtTemaId.getText().trim());
            Tema tema = temaDAO.buscarPorId(id);

            if (tema != null) {
                preencherCampos(tema);
                selecionarNaTabela(id);
            } else {
                exibirAlerta(Alert.AlertType.INFORMATION, "Não encontrado", "Tema com ID " + id + " não localizado.");
                txtTemaNome.clear();
            }

        } catch (NumberFormatException e) {
            erro("ID inválido.");
        }
    }

    private void selecionarNaTabela(int id) {
        temaData.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .ifPresent(t -> {
                    tblTemas.getSelectionModel().select(t);
                    tblTemas.scrollTo(t);
                });
    }

    private void estadoInicial() {
        btnTemaSalvar.setDisable(false);
        btnTemaAtualizar.setDisable(true);
        btnTemaExcluir.setDisable(true);
        txtTemaNome.requestFocus();
        carregarTemas();
    }

    private void sucesso(String mensagem) {
        exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", mensagem);
        estadoInicial();
        handleLimparTema();
    }

    private void erro(String mensagem) {
        exibirAlerta(Alert.AlertType.ERROR, "Erro", mensagem);
    }

    private boolean confirmar(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, mensagem, ButtonType.YES, ButtonType.NO);
        alerta.setTitle("Confirmação");
        alerta.setHeaderText(null);
        Optional<ButtonType> resultado = alerta.showAndWait();
        return resultado.isPresent() && resultado.get() == ButtonType.YES;
    }

    private void exibirAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
