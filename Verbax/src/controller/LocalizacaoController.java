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

// Controlador responsável pela gestão das Localizações, incluindo operações de inserir, atualizar, excluir e buscar.
public class LocalizacaoController {

    // Campos de texto para exibição e entrada dos dados da localização
    @FXML
    private TextField txtLocalizacaoId;            // Campo que contém o ID da localização
    @FXML
    private TextField txtLocalizacaoSetor;         // Campo que contém o setor da localização
    @FXML
    private TextField txtLocalizacaoPrateleira;      // Campo que contém a prateleira da localização

    // Botões para executar as operações de salvar, atualizar, excluir, limpar e buscar localização
    @FXML
    private Button btnLocalizacaoSalvar;
    @FXML
    private Button btnLocalizacaoAtualizar;
    @FXML
    private Button btnLocalizacaoExcluir;
    @FXML
    private Button btnLocalizacaoLimpar;
    @FXML
    private Button btnLocalizacaoBuscar;

    // Componentes da TableView para exibir a lista de localizações
    @FXML
    private TableView<Localizacao> tblLocalizacoes;   // Tabela que lista as localizações
    @FXML
    private TableColumn<Localizacao, Integer> colLocalizacaoId;          // Coluna para exibir o ID
    @FXML
    private TableColumn<Localizacao, String> colLocalizacaoSetor;        // Coluna para exibir o setor
    @FXML
    private TableColumn<Localizacao, String> colLocalizacaoPrateleira;     // Coluna para exibir a prateleira

    // Instância do DAO para efetuar as operações na base de dados
    private final LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO();
    // Lista observável que armazena os dados das localizações e sincroniza a TableView
    private final ObservableList<Localizacao> localizacaoData = FXCollections.observableArrayList();

    /**
     * Método de inicialização chamado automaticamente após a injeção dos componentes FXML.
     * Configura a tabela, o comportamento de seleção, carrega os dados e define o estado inicial dos botões.
     */
    @FXML
    public void initialize() {
        configurarTabela();
        configurarSelecaoTabela();
        carregarLocalizacoes();
        estadoInicialBotoes();
    }

    /**
     * Configura a TableView associando as colunas às propriedades da classe Localizacao.
     */
    private void configurarTabela() {
        // Define a propriedade "id" para a coluna colLocalizacaoId
        colLocalizacaoId.setCellValueFactory(new PropertyValueFactory<>("id"));
        // Define a propriedade "setor" para a coluna colLocalizacaoSetor
        colLocalizacaoSetor.setCellValueFactory(new PropertyValueFactory<>("setor"));
        // Define a propriedade "prateleira" para a coluna colLocalizacaoPrateleira
        colLocalizacaoPrateleira.setCellValueFactory(new PropertyValueFactory<>("prateleira"));
        // Associa a lista de dados à tabela
        tblLocalizacoes.setItems(localizacaoData);
    }

    /**
     * Configura o listener para a seleção de itens na TableView.
     * Quando um item é selecionado, os campos do formulário são preenchidos com os dados correspondentes.
     */
    private void configurarSelecaoTabela() {
        tblLocalizacoes.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> preencherCampos(newSelection));
    }

    /**
     * Carrega as localizações obtidas do DAO para a lista observável e atualiza a TableView.
     */
    private void carregarLocalizacoes() {
        localizacaoData.setAll(localizacaoDAO.listarTodos());
        tblLocalizacoes.refresh();
    }

    /**
     * Define o estado inicial dos botões, desabilitando os botões de atualizar e excluir.
     */
    private void estadoInicialBotoes() {
        btnLocalizacaoAtualizar.setDisable(true);
        btnLocalizacaoExcluir.setDisable(true);
    }

    /**
     * Preenche os campos de entrada com os dados do objeto Localizacao selecionado.
     * Se o parâmetro for nulo, limpa os campos.
     *
     * @param loc A localização selecionada na tabela.
     */
    private void preencherCampos(Localizacao loc) {
        if (loc == null) {
            handleLimparLocalizacao();
            return;
        }

        // Popula os campos com os valores do objeto selecionado
        txtLocalizacaoId.setText(String.valueOf(loc.getId()));
        txtLocalizacaoSetor.setText(loc.getSetor());
        txtLocalizacaoPrateleira.setText(loc.getPrateleira());

        // Ajusta os estados dos componentes: desabilita a edição do ID e habilita os botões de atualizar e excluir
        txtLocalizacaoId.setDisable(true);
        btnLocalizacaoSalvar.setDisable(true);
        btnLocalizacaoAtualizar.setDisable(false);
        btnLocalizacaoExcluir.setDisable(false);
    }

    /**
     * Valida os campos obrigatórios passados como parâmetros.
     *
     * @param campos Lista de strings que devem ser validadas.
     * @return true se todos os campos tiverem valores; false caso algum esteja vazio.
     */
    private boolean validarCampos(String... campos) {
        for (String campo : campos) {
            if (campo == null || campo.trim().isEmpty()) return false;
        }
        return true;
    }

    /**
     * Tenta converter a String fornecida para um número inteiro representando o ID.
     *
     * @param idStr A String com o valor do ID.
     * @return Um Optional contendo o número inteiro se a conversão for bem sucedida; Optional.empty() caso contrário.
     */
    private Optional<Integer> parseId(String idStr) {
        try {
            return Optional.of(Integer.parseInt(idStr));
        } catch (NumberFormatException e) {
            exibirAlerta(Alert.AlertType.ERROR, "ID inválido", "O ID informado deve ser um número.");
            return Optional.empty();
        }
    }

    /**
     * Manipulador do evento de salvar uma nova localização.
     * Valida os campos, cria um novo objeto Localizacao e o insere na base de dados.
     */
    @FXML
    private void handleSalvarLocalizacao() {
        // Obtém os valores do setor e da prateleira a partir dos campos de texto
        String setor = txtLocalizacaoSetor.getText();
        String prateleira = txtLocalizacaoPrateleira.getText();

        // Valida se os campos obrigatórios foram preenchidos
        if (!validarCampos(setor, prateleira)) {
            exibirAlerta(Alert.AlertType.ERROR, "Validação", "Setor e Prateleira são obrigatórios.");
            return;
        }

        // Cria um objeto Localizacao com id zero (indicando novo registo)
        Localizacao loc = new Localizacao(0, setor, prateleira);
        // Tenta inserir a localização e exibe uma mensagem conforme o resultado
        if (localizacaoDAO.inserir(loc)) {
            exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Localização salva com sucesso.");
            carregarLocalizacoes();
            handleLimparLocalizacao();
        } else {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao salvar no banco de dados.");
        }
    }

    /**
     * Manipulador do evento de atualizar uma localização existente.
     * Converte o ID, valida os campos e chama o DAO para realizar a atualização.
     */
    @FXML
    private void handleAtualizarLocalizacao() {
        // Converte o valor do ID para um inteiro
        Optional<Integer> optId = parseId(txtLocalizacaoId.getText());
        if (optId.isEmpty()) return;

        // Obtém os valores atualizados para setor e prateleira
        String setor = txtLocalizacaoSetor.getText();
        String prateleira = txtLocalizacaoPrateleira.getText();

        // Valida se os campos estão preenchidos
        if (!validarCampos(setor, prateleira)) {
            exibirAlerta(Alert.AlertType.ERROR, "Validação", "Todos os campos são obrigatórios.");
            return;
        }

        // Cria um objeto Localizacao com os dados atualizados
        Localizacao loc = new Localizacao(optId.get(), setor, prateleira);
        // Se a atualização for bem sucedida, exibe alerta e recarrega os dados
        if (localizacaoDAO.atualizar(loc)) {
            exibirAlerta(Alert.AlertType.INFORMATION, "Atualizado", "Localização atualizada com sucesso.");
            carregarLocalizacoes();
            handleLimparLocalizacao();
        } else {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao atualizar. Verifique se o ID existe.");
        }
    }

    /**
     * Manipulador do evento de excluir uma localização.
     * Converte o ID, solicita confirmação e, em caso afirmativo, exclui a localização através do DAO.
     */
    @FXML
    private void handleExcluirLocalizacao() {
        // Tenta converter o ID informado para inteiro
        Optional<Integer> optId = parseId(txtLocalizacaoId.getText());
        if (optId.isEmpty()) return;

        // Exibe uma caixa de diálogo de confirmação para a exclusão
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION,
                "Deseja realmente excluir a localização com ID " + optId.get() + "?",
                ButtonType.YES, ButtonType.NO);
        confirmacao.setTitle("Confirmar Exclusão");

        // Se o usuário confirmar a exclusão, tenta excluir o registo
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

    /**
     * Manipulador do evento de buscar uma localização pelo ID.
     * Converte o ID, busca a localização e preenche os campos do formulário se encontrada.
     */
    @FXML
    private void handleBuscarLocalizacaoPorId() {
        // Tenta converter o ID e, se bem sucedido, busca a localização
        Optional<Integer> optId = parseId(txtLocalizacaoId.getText());
        if (optId.isEmpty()) return;

        Localizacao loc = localizacaoDAO.buscarPorId(optId.get());
        if (loc != null) {
            // Se a localização for encontrada, preenche os campos e seleciona o registo na tabela
            preencherCampos(loc);
            tblLocalizacoes.getItems().stream()
                    .filter(item -> item.getId() == loc.getId())
                    .findFirst()
                    .ifPresent(item -> {
                        tblLocalizacoes.getSelectionModel().select(item);
                        tblLocalizacoes.scrollTo(item);
                    });
        } else {
            // Se não encontrada, exibe uma mensagem informativa e limpa os campos de setor e prateleira
            exibirAlerta(Alert.AlertType.INFORMATION, "Não encontrado", "Localização com ID " + optId.get() + " não encontrada.");
            txtLocalizacaoSetor.clear();
            txtLocalizacaoPrateleira.clear();
        }
    }

    /**
     * Manipulador do evento de limpar os campos do formulário.
     * Restaura os campos ao estado inicial e prepara a interface para a inserção de um novo registo.
     */
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

    /**
     * Exibe um alerta na interface com o tipo, título e mensagem especificados.
     *
     * @param tipo     Tipo de alerta (INFORMATION, ERROR, etc.)
     * @param titulo   Título do alerta
     * @param mensagem Mensagem a ser exibida
     */
    private void exibirAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}
