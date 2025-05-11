package controller; // Define o pacote onde se encontra o controlador

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

/**
 * Classe controlador responsável pela interface de gestão de Autores.
 * Integra as funcionalidades de criação, atualização, exclusão e procura dos autores.
 */
public class AutorController {

    // Componentes injetados a partir do ficheiro FXML
    @FXML
    private TextField txtAutorId; // Campo de texto para o ID do autor (desativado para edição direta)
    @FXML
    private TextField txtAutorNome; // Campo de texto para o nome do autor
    @FXML
    private TextField txtAutorNacionalidade; // Campo de texto para a nacionalidade do autor
    @FXML
    private TableView<Autor> tblAutores; // Tabela para exibir a lista de autores
    @FXML
    private TableColumn<Autor, Integer> colAutorId; // Coluna da tabela para exibir o ID
    @FXML
    private TableColumn<Autor, String> colAutorNome; // Coluna da tabela para exibir o nome
    @FXML
    private TableColumn<Autor, String> colAutorNacionalidade; // Coluna da tabela para exibir a nacionalidade

    // Instância do DAO para efetuar operações na base de dados
    private final AutorDAO autorDAO = new AutorDAO();
    // Lista observável que armazena os autores para atualizar a TableView dinamicamente
    private final ObservableList<Autor> listaAutores = FXCollections.observableArrayList();
    // Logger para registar eventos e erros
    private static final Logger logger = Logger.getLogger(AutorController.class.getName());

    /**
     * Método invocado automaticamente após a injeção dos componentes FXML.
     * Configura as colunas da tabela, carrega os autores e define o comportamento da seleção.
     */
    @FXML
    private void initialize() {
        // Define a forma de obter os valores para a coluna de ID
        colAutorId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        // Define a forma de obter os valores para a coluna de Nome
        colAutorNome.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));
        // Define a forma de obter os valores para a coluna de Nacionalidade
        colAutorNacionalidade.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNacionalidade()));

        // Associa à tabela a lista observável dos autores
        tblAutores.setItems(listaAutores);
        // Carrega os autores da base de dados para a tabela
        carregarAutores();
        // Configura o comportamento da seleção de um registo na tabela
        configurarSelecaoTabela();
        // Desativa o campo do ID, pois este deve ser preenchido automaticamente
        txtAutorId.setDisable(true);
    }

    /**
     * Carrega todos os registos de autores da base de dados e atualiza a lista observável.
     */
    private void carregarAutores() {
        listaAutores.setAll(autorDAO.listarTodos());
    }

    /**
     * Configura a ação de seleção de um autor na tabela.
     * Quando um autor é selecionado, os seus dados são apresentados nos campos de texto.
     */
    private void configurarSelecaoTabela() {
        tblAutores.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                txtAutorId.setText(String.valueOf(newSel.getId()));
                txtAutorNome.setText(newSel.getNome());
                txtAutorNacionalidade.setText(newSel.getNacionalidade());
            }
        });
    }

    /**
     * Manipulador do evento de salvar um novo autor.
     * Valida os campos, insere o autor na base de dados e atualiza a tabela.
     */
    @FXML
    private void handleSalvarAutor() {
        // Obtém e limpe os espaços em branco dos valores dos campos de nome e nacionalidade
        String nome = txtAutorNome.getText().trim();
        String nacionalidade = txtAutorNacionalidade.getText().trim();

        // Validação: Ambos os campos devem ser preenchidos
        if (nome.isEmpty() || nacionalidade.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Erro de Validação", "Nome e Nacionalidade são obrigatórios.");
            return;
        }

        // Cria um novo objeto Autor e define os seus atributos
        Autor autor = new Autor();
        autor.setNome(nome);
        autor.setNacionalidade(nacionalidade);

        // Tenta inserir o autor na base de dados e apresenta uma mensagem de sucesso ou erro
        if (autorDAO.inserir(autor)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Autor salvo com sucesso.");
            limparCampos();
            carregarAutores();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao salvar autor.");
        }
    }

    /**
     * Manipulador do evento de atualização dos dados de um autor existente.
     * Valida os campos, converte o ID para inteiro e tenta atualizar o autor na base de dados.
     */
    @FXML
    private void handleAtualizarAutor() {
        String idStr = txtAutorId.getText().trim();
        String nome = txtAutorNome.getText().trim();
        String nacionalidade = txtAutorNacionalidade.getText().trim();

        // Validação: Todos os campos devem estar preenchidos para a atualização
        if (idStr.isEmpty() || nome.isEmpty() || nacionalidade.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Erro de Validação", "Todos os campos devem ser preenchidos para atualização.");
            return;
        }

        try {
            // Converte o ID para inteiro
            int id = Integer.parseInt(idStr);
            // Cria um objeto Autor com os dados atualizados
            Autor autor = new Autor(id, nome, nacionalidade);

            // Tenta atualizar o autor na base de dados e apresenta a mensagem correspondente
            if (autorDAO.atualizar(autor)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Autor atualizado com sucesso.");
                limparCampos();
                carregarAutores();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao atualizar autor.");
            }
        } catch (NumberFormatException e) {
            // Regista o erro se o ID não for um número válido
            logger.log(Level.SEVERE, "ID inválido para atualização: " + idStr, e);
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "ID inválido para atualização.");
        }
    }

    /**
     * Manipulador do evento de exclusão de um autor.
     * Valida o campo do ID, converte-o para inteiro e tenta eliminar o autor da base de dados.
     */
    @FXML
    private void handleExcluirAutor() {
        String idStr = txtAutorId.getText().trim();

        // Validação: O ID deve ser informado para a exclusão
        if (idStr.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Erro de Validação", "Informe o ID do autor a ser excluído.");
            return;
        }

        try {
            // Converte o ID para inteiro
            int id = Integer.parseInt(idStr);

            // Tenta excluir o autor e apresenta a mensagem de resultado
            if (autorDAO.excluir(id)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Autor excluído com sucesso.");
                limparCampos();
                carregarAutores();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao excluir autor.");
            }
        } catch (NumberFormatException e) {
            // Regista e trata o erro se o ID não for numérico
            logger.log(Level.SEVERE, "ID inválido para exclusão: " + idStr, e);
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "ID inválido para exclusão.");
        }
    }

    /**
     * Manipulador do evento de busca de um autor por ID.
     * Exibe um diálogo para solicitar o ID, efetua a busca e preenche os campos se o autor for encontrado.
     */
    @FXML
    private void handleBuscarAutorPorId() {
        // Cria um diálogo para introdução do ID
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Buscar Autor");
        dialog.setHeaderText("Buscar autor por ID");
        dialog.setContentText("Informe o ID do autor:");

        // Mostra o diálogo e processa o ID inserido pelo utilizador
        dialog.showAndWait().ifPresent(idStr -> {
            try {
                // Converte o ID para inteiro
                int id = Integer.parseInt(idStr);
                // Procura o autor na base de dados
                Autor autor = autorDAO.buscarPorId(id);
                if (autor != null) {
                    // Se encontrado, preenche os campos com os dados do autor
                    txtAutorId.setText(String.valueOf(autor.getId()));
                    txtAutorNome.setText(autor.getNome());
                    txtAutorNacionalidade.setText(autor.getNacionalidade());
                } else {
                    // Caso não seja encontrado, informa o utilizador
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Resultado", "Autor não encontrado.");
                }
            } catch (NumberFormatException e) {
                // Regista o erro se o ID for inválido
                logger.log(Level.WARNING, "ID inválido na busca: " + idStr, e);
                mostrarAlerta(Alert.AlertType.ERROR, "Erro", "ID inválido.");
            }
        });
    }

    /**
     * Manipulador do evento de limpeza dos campos da interface.
     * Limpa os campos de texto e remove a seleção atual da tabela.
     */
    @FXML
    private void handleLimparAutor() {
        limparCampos();
        tblAutores.getSelectionModel().clearSelection();
    }

    /**
     * Método auxiliar que limpa os campos de entrada.
     */
    private void limparCampos() {
        txtAutorId.clear();
        txtAutorNome.clear();
        txtAutorNacionalidade.clear();
    }

    /**
     * Método auxiliar para exibir alertas com base no tipo, título e mensagem.
     *
     * @param tipo     Tipo de alerta (INFORMATION, WARNING, ERROR, etc.)
     * @param titulo   Título do alerta
     * @param mensagem Mensagem a ser exibida no alerta
     */
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null); // Sem cabeçalho para o alerta
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
