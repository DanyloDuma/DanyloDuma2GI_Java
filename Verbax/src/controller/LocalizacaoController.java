package controller;

import dao.LocalizacaoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Localizacao;

import java.util.List;
import java.util.Optional;

public class LocalizacaoController {

    @FXML private TextField txtLocalizacaoId;
    @FXML private TextField txtLocalizacaoSetor;
    @FXML private TextField txtLocalizacaoPrateleira;

    @FXML private Button btnLocalizacaoSalvar;
    @FXML private Button btnLocalizacaoAtualizar;
    @FXML private Button btnLocalizacaoExcluir;
    @FXML private Button btnLocalizacaoLimpar;
    @FXML private Button btnLocalizacaoBuscar;

    @FXML private TableView<Localizacao> tblLocalizacoes;
    @FXML private TableColumn<Localizacao, Integer> colLocalizacaoId;
    @FXML private TableColumn<Localizacao, String> colLocalizacaoSetor;
    @FXML private TableColumn<Localizacao, String> colLocalizacaoPrateleira;

    private final LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO();
    private final ObservableList<Localizacao> localizacaoData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configurarTabela();
        configurarSelecaoTabela();
        carregarLocalizacoes();
        estadoInicialBotoes();
    }

    private void configurarTabela() {
        colLocalizacaoId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colLocalizacaoSetor.setCellValueFactory(new PropertyValueFactory<>("setor"));
        colLocalizacaoPrateleira.setCellValueFactory(new PropertyValueFactory<>("prateleira"));
        tblLocalizacoes.setItems(localizacaoData);
    }

    private void configurarSelecaoTabela() {
        tblLocalizacoes.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> preencherCampos(newSelection));
    }

    private void carregarLocalizacoes() {
        localizacaoData.setAll(localizacaoDAO.listarTodos());
        tblLocalizacoes.refresh();
    }

    private void estadoInicialBotoes() {
        btnLocalizacaoAtualizar.setDisable(true);
        btnLocalizacaoExcluir.setDisable(true);
    }

    private void preencherCampos(Localizacao loc) {
        if (loc == null) {
            handleLimparLocalizacao();
            return;
        }

        txtLocalizacaoId.setText(String.valueOf(loc.getId()));
        txtLocalizacaoSetor.setText(loc.getSetor());
        txtLocalizacaoPrateleira.setText(loc.getPrateleira());

        txtLocalizacaoId.setDisable(true);
        btnLocalizacaoSalvar.setDisable(true);
        btnLocalizacaoAtualizar.setDisable(false);
        btnLocalizacaoExcluir.setDisable(false);
    }

    private boolean validarCampos(String... campos) {
        for (String campo : campos) {
            if (campo == null || campo.trim().isEmpty()) return false;
        }
        return true;
    }

    private Optional<Integer> parseId(String idStr) {
        try {
            return Optional.of(Integer.parseInt(idStr));
        } catch (NumberFormatException e) {
            exibirAlerta(Alert.AlertType.ERROR, "ID inválido", "O ID informado deve ser um número.");
            return Optional.empty();
        }
    }

    @FXML
    private void handleSalvarLocalizacao() {
        String setor = txtLocalizacaoSetor.getText();
        String prateleira = txtLocalizacaoPrateleira.getText();

        if (!validarCampos(setor, prateleira)) {
            exibirAlerta(Alert.AlertType.ERROR, "Validação", "Setor e Prateleira são obrigatórios.");
            return;
        }

        Localizacao loc = new Localizacao(0, setor, prateleira);
        if (localizacaoDAO.inserir(loc)) {
            exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Localização salva com sucesso.");
            carregarLocalizacoes();
            handleLimparLocalizacao();
        } else {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao salvar no banco de dados.");
        }
    }

    @FXML
    private void handleAtualizarLocalizacao() {
        Optional<Integer> optId = parseId(txtLocalizacaoId.getText());
        if (optId.isEmpty()) return;

        String setor = txtLocalizacaoSetor.getText();
        String prateleira = txtLocalizacaoPrateleira.getText();

        if (!validarCampos(setor, prateleira)) {
            exibirAlerta(Alert.AlertType.ERROR, "Validação", "Todos os campos são obrigatórios.");
            return;
        }

        Localizacao loc = new Localizacao(optId.get(), setor, prateleira);
        if (localizacaoDAO.atualizar(loc)) {
            exibirAlerta(Alert.AlertType.INFORMATION, "Atualizado", "Localização atualizada com sucesso.");
            carregarLocalizacoes();
            handleLimparLocalizacao();
        } else {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao atualizar. Verifique se o ID existe.");
        }
    }

    @FXML
    private void handleExcluirLocalizacao() {
        Optional<Integer> optId = parseId(txtLocalizacaoId.getText());
        if (optId.isEmpty()) return;

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION,
                "Deseja realmente excluir a localização com ID " + optId.get() + "?",
                ButtonType.YES, ButtonType.NO);
        confirmacao.setTitle("Confirmar Exclusão");

        confirmacao.showAndWait().ifPresent(resposta -> {
            if (resposta == ButtonType.YES) {
                if (localizacaoDAO.excluir(optId.get())) {
                    exibirAlerta(Alert.AlertType.INFORMATION, "Excluído", "Localização excluída com sucesso.");
                    carregarLocalizacoes();
                    handleLimparLocalizacao();
                } else {
                    exibirAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao excluir. Verifique o ID.");
                }
            }
        });
    }

    @FXML
    private void handleBuscarLocalizacaoPorId() {
        Optional<Integer> optId = parseId(txtLocalizacaoId.getText());
        if (optId.isEmpty()) return;

        Localizacao loc = localizacaoDAO.buscarPorId(optId.get());
        if (loc != null) {
            preencherCampos(loc);
            tblLocalizacoes.getItems().stream()
                    .filter(item -> item.getId() == loc.getId())
                    .findFirst()
                    .ifPresent(item -> {
                        tblLocalizacoes.getSelectionModel().select(item);
                        tblLocalizacoes.scrollTo(item);
                    });
        } else {
            exibirAlerta(Alert.AlertType.INFORMATION, "Não encontrado", "Localização com ID " + optId.get() + " não encontrada.");
            txtLocalizacaoSetor.clear();
            txtLocalizacaoPrateleira.clear();
        }
    }

    @FXML
    private void handleLimparLocalizacao() {
        txtLocalizacaoId.clear();
        txtLocalizacaoSetor.clear();
        txtLocalizacaoPrateleira.clear();
        txtLocalizacaoId.setDisable(false);

        tblLocalizacoes.getSelectionModel().clearSelection();
        estadoInicialBotoes();
        btnLocalizacaoSalvar.setDisable(false);
        txtLocalizacaoSetor.requestFocus();
    }

    private void exibirAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}
