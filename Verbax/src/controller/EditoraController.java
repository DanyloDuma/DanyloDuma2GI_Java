package controller;

import dao.EditoraDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import model.Editora;

/**
 * Classe controlador para a gestão de Editoras.
 * Responsável por apresentar, inserir, atualizar, excluir e procurar editoras na interface.
 */
public class EditoraController {

    // Componentes injetados a partir do ficheiro FXML
    @FXML
    private TextField txtEditoraId;       // Campo para o ID da editora (geralmente desativado para edição direta)
    @FXML
    private TextField txtEditoraNome;     // Campo para o nome da editora
    @FXML
    private TextField txtEditoraCidade;   // Campo para a cidade da editora

    @FXML
    private TableView<Editora> tblEditoras;  // Tabela para exibir as editoras
    @FXML
    private TableColumn<Editora, Integer> colEditoraId;      // Coluna para exibir o ID
    @FXML
    private TableColumn<Editora, String> colEditoraNome;       // Coluna para exibir o nome
    @FXML
    private TableColumn<Editora, String> colEditoraCidade;     // Coluna para exibir a cidade

    // Instância do DAO que efetua operações na base de dados
    private final EditoraDAO editoraDAO = new EditoraDAO();
    // Lista observável que mantém as editoras e sincroniza a tabela automaticamente
    private final ObservableList<Editora> listaEditoras = FXCollections.observableArrayList();

    /**
     * Método de inicialização do controlador.
     * Configura as colunas da tabela, carrega as editoras e define ações ao clicar na tabela.
     */
    @FXML
    private void initialize() {
        configurarColunasTabela();
        carregarEditoras();
        // Define o evento de clique na tabela para selecionar uma editora
        tblEditoras.setOnMouseClicked(this::selecionarEditoraDaTabela);
    }

    /**
     * Configura as colunas da TableView definindo como obter os valores de cada propriedade.
     */
    private void configurarColunasTabela() {
        colEditoraId.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getId()).asObject());
        colEditoraNome.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getNome()));
        colEditoraCidade.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getCidade()));
    }

    /**
     * Carrega todas as editoras da base de dados e atualiza a lista observável.
     */
    private void carregarEditoras() {
        listaEditoras.setAll(editoraDAO.listarTodos());
        tblEditoras.setItems(listaEditoras);
    }

    /**
     * Manipulador do evento de salvar uma nova editora.
     * Valida os campos obrigatórios, insere a editora na base de dados e atualiza a tabela.
     */
    @FXML
    private void handleSalvarEditora() {
        if (!validarCamposObrigatorios()) return;

        // Cria uma nova instância de Editora com ID 0 (novo registo) e os dados introduzidos
        Editora novaEditora = new Editora(0, txtEditoraNome.getText(), txtEditoraCidade.getText());

        if (editoraDAO.inserir(novaEditora)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Editora salva com sucesso.");
            limparCampos();
            carregarEditoras();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao salvar editora.");
        }
    }

    /**
     * Manipulador do evento de atualizar uma editora existente.
     * Valida os campos obrigatórios, converte o ID e tenta atualizar no DAO.
     */
    @FXML
    private void handleAtualizarEditora() {
        // Verifica se os campos obrigatórios e o ID estão preenchidos
        if (!validarCamposObrigatorios() || txtEditoraId.getText().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Aviso", "Informe o ID da editora para atualizar.");
            return;
        }

        try {
            // Converte o ID para inteiro e cria um objeto Editora com os dados atualizados
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

    /**
     * Manipulador do evento de excluir uma editora.
     * Converte o ID, solicita confirmação e, se confirmada, exclui a editora.
     */
    @FXML
    private void handleExcluirEditora() {
        try {
            int id = Integer.parseInt(txtEditoraId.getText());

            // Solicita confirmação da exclusão ao utilizador
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

    /**
     * Manipulador do evento de buscar uma editora por ID.
     * Tenta converter o ID e preencher os campos com os dados da editora encontrada.
     */
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

    /**
     * Manipulador do evento para limpar os campos da interface.
     */
    @FXML
    private void handleLimparEditora() {
        limparCampos();
    }

    /**
     * Método auxiliar que limpa os campos de entrada e a seleção da tabela.
     */
    private void limparCampos() {
        txtEditoraId.clear();
        txtEditoraNome.clear();
        txtEditoraCidade.clear();
        tblEditoras.getSelectionModel().clearSelection();
    }

    /**
     * Valida se os campos obrigatórios (Nome e Cidade) foram preenchidos.
     *
     * @return true se os campos estiverem preenchidos; false caso contrário.
     */
    private boolean validarCamposObrigatorios() {
        String nome = txtEditoraNome.getText();
        String cidade = txtEditoraCidade.getText();

        if (nome.isEmpty() || cidade.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Aviso", "Nome e Cidade são obrigatórios.");
            return false;
        }

        return true;
    }

    /**
     * Método auxiliar para exibir alertas com base no tipo, título e mensagem.
     *
     * @param tipo     Tipo de alerta (INFORMATION, WARNING, ERROR, etc.)
     * @param titulo   Título do alerta
     * @param mensagem Mensagem a ser exibida
     */
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    /**
     * Evento que ocorre quando se clica numa linha da tabela.
     * Seleciona a editora e preenche os campos com os seus dados.
     *
     * @param event Evento de clique do rato
     */
    private void selecionarEditoraDaTabela(MouseEvent event) {
        Editora selecionada = tblEditoras.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            txtEditoraId.setText(String.valueOf(selecionada.getId()));
            txtEditoraNome.setText(selecionada.getNome());
            txtEditoraCidade.setText(selecionada.getCidade());
        }
    }
}
