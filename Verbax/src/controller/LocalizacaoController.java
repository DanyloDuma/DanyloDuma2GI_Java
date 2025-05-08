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

    private LocalizacaoDAO localizacaoDAO;
    private ObservableList<Localizacao> localizacaoData;

    @FXML
    public void initialize() {
        localizacaoDAO = new LocalizacaoDAO();

        // Configura as colunas da tabela
        colLocalizacaoId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colLocalizacaoSetor.setCellValueFactory(new PropertyValueFactory<>("setor"));
        colLocalizacaoPrateleira.setCellValueFactory(new PropertyValueFactory<>("prateleira"));

        localizacaoData = FXCollections.observableArrayList();
        tblLocalizacoes.setItems(localizacaoData);

        // Adiciona um listener para a seleção de itens na tabela
        tblLocalizacoes.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> preencherCampos(newValue));

        // Carrega os dados iniciais na tabela
        carregarLocalizacoes();

        // Define o estado inicial dos botões (opcional, mas bom para UX)
        btnLocalizacaoAtualizar.setDisable(true);
        btnLocalizacaoExcluir.setDisable(true);
    }

    private void carregarLocalizacoes() {
        localizacaoData.clear();
        List<Localizacao> localizacoes = localizacaoDAO.listarTodos();
        localizacaoData.addAll(localizacoes);
        tblLocalizacoes.refresh(); // Garante que a tabela seja atualizada visualmente
    }

    private void preencherCampos(Localizacao loc) {
        if (loc != null) {
            txtLocalizacaoId.setText(String.valueOf(loc.getId()));
            txtLocalizacaoSetor.setText(loc.getSetor());
            txtLocalizacaoPrateleira.setText(loc.getPrateleira());

            txtLocalizacaoId.setDisable(true); // ID não deve ser editado diretamente após seleção/busca
            btnLocalizacaoSalvar.setDisable(true); // Evita salvar duplicado
            btnLocalizacaoAtualizar.setDisable(false); // Permite atualizar
            btnLocalizacaoExcluir.setDisable(false); // Permite excluir
        } else {
            handleLimparLocalizacao(); // Chama limpar se nada for selecionado (ou desselecionado)
        }
    }

    @FXML
    private void handleSalvarLocalizacao() {
        String setor = txtLocalizacaoSetor.getText();
        String prateleira = txtLocalizacaoPrateleira.getText();

        if (setor.isEmpty() || prateleira.isEmpty()) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro de Validação", "Os campos Setor e Prateleira são obrigatórios.");
            return;
        }

        // ID é gerado pelo banco de dados, então passamos 0 ou um construtor sem ID.
        // O DAO.inserir não utiliza o ID do objeto para a query de inserção.
        Localizacao novaLocalizacao = new Localizacao(0, setor, prateleira);
        if (localizacaoDAO.inserir(novaLocalizacao)) {
            exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Localização salva com sucesso!");
            carregarLocalizacoes();
            handleLimparLocalizacao();
        } else {
            exibirAlerta(Alert.AlertType.ERROR, "Erro ao Salvar", "Falha ao salvar a localização no banco de dados.");
        }
    }

    @FXML
    private void handleAtualizarLocalizacao() {
        String idStr = txtLocalizacaoId.getText();
        String setor = txtLocalizacaoSetor.getText();
        String prateleira = txtLocalizacaoPrateleira.getText();

        if (idStr.isEmpty() || setor.isEmpty() || prateleira.isEmpty()) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro de Validação", "Todos os campos (ID, Setor, Prateleira) são obrigatórios para atualização.");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            Localizacao localizacao = new Localizacao(id, setor, prateleira);
            if (localizacaoDAO.atualizar(localizacao)) {
                exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Localização atualizada com sucesso!");
                carregarLocalizacoes();
                handleLimparLocalizacao();
            } else {
                exibirAlerta(Alert.AlertType.ERROR, "Erro ao Atualizar", "Falha ao atualizar a localização. Verifique se o ID é válido e existe.");
            }
        } catch (NumberFormatException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro de Formato", "O ID da localização deve ser um número válido.");
        }
    }

    @FXML
    private void handleExcluirLocalizacao() {
        String idStr = txtLocalizacaoId.getText();
        Localizacao selecionada = tblLocalizacoes.getSelectionModel().getSelectedItem();

        if (idStr.isEmpty() && selecionada == null) {
            exibirAlerta(Alert.AlertType.WARNING, "Nenhuma Seleção", "Por favor, digite um ID ou selecione uma localização na tabela para excluir.");
            return;
        }

        int idParaExcluir;
        if (!idStr.isEmpty()) {
            try {
                idParaExcluir = Integer.parseInt(idStr);
            } catch (NumberFormatException e) {
                exibirAlerta(Alert.AlertType.ERROR, "Erro de Formato", "O ID fornecido para exclusão é inválido.");
                return;
            }
        } else { // selecionada != null
            idParaExcluir = selecionada.getId();
        }

        Alert alertConfirmacao = new Alert(Alert.AlertType.CONFIRMATION,
                "Tem certeza que deseja excluir a localização com ID " + idParaExcluir + "?",
                ButtonType.YES, ButtonType.NO);
        alertConfirmacao.setTitle("Confirmar Exclusão");
        alertConfirmacao.setHeaderText(null); // Sem cabeçalho
        Optional<ButtonType> resultado = alertConfirmacao.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.YES) {
            if (localizacaoDAO.excluir(idParaExcluir)) {
                exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Localização excluída com sucesso!");
                carregarLocalizacoes();
                handleLimparLocalizacao();
            } else {
                exibirAlerta(Alert.AlertType.ERROR, "Erro ao Excluir", "Falha ao excluir a localização. Verifique se o ID é válido e existe.");
            }
        }
    }

    @FXML
    private void handleLimparLocalizacao() {
        txtLocalizacaoId.clear();
        txtLocalizacaoSetor.clear();
        txtLocalizacaoPrateleira.clear();
        tblLocalizacoes.getSelectionModel().clearSelection();

        txtLocalizacaoId.setDisable(false); // Permite digitar ID para busca ou novo (se aplicável)
        btnLocalizacaoSalvar.setDisable(false); // Permite salvar
        btnLocalizacaoAtualizar.setDisable(true); // Desabilita atualizar ao limpar
        btnLocalizacaoExcluir.setDisable(true); // Desabilita excluir ao limpar

        txtLocalizacaoSetor.requestFocus(); // Foco no campo setor para nova entrada
    }

    @FXML
    private void handleBuscarLocalizacaoPorId() {
        String idStr = txtLocalizacaoId.getText();
        if (idStr.isEmpty()) {
            exibirAlerta(Alert.AlertType.WARNING, "Campo Vazio", "Por favor, insira um ID para buscar.");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            Localizacao loc = localizacaoDAO.buscarPorId(id);
            if (loc != null) {
                // Preenche os campos com os dados encontrados
                preencherCampos(loc); // Isso também desabilitará o campo ID e ajustará botões

                // Seleciona e foca o item na tabela (se existir)
                tblLocalizacoes.getItems().stream()
                        .filter(item -> item.getId() == id)
                        .findFirst()
                        .ifPresent(item -> {
                            tblLocalizacoes.getSelectionModel().select(item);
                            tblLocalizacoes.scrollTo(item);
                        });
            } else {
                exibirAlerta(Alert.AlertType.INFORMATION, "Não Encontrado", "Localização com ID " + id + " não encontrada.");
                // Limpa os campos de setor e prateleira, mas mantém o ID digitado para possível cadastro ou nova busca.
                txtLocalizacaoSetor.clear();
                txtLocalizacaoPrateleira.clear();
                txtLocalizacaoId.setDisable(false); // Permite editar o ID para nova busca
                btnLocalizacaoSalvar.setDisable(false);
                btnLocalizacaoAtualizar.setDisable(true);
                btnLocalizacaoExcluir.setDisable(true);
            }
        } catch (NumberFormatException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro de Formato", "O ID da localização deve ser um número válido.");
        }
    }

    private void exibirAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null); // Sem cabeçalho, a menos que desejado
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}