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

// Controlador responsável pela gestão dos Temas.
// Permite criar, atualizar, excluir, buscar e limpar dados relativos à entidade Tema.
public class TemaController {

    // Componentes FXML para entrada de dados
    @FXML
    private TextField txtTemaId;    // Campo de texto para o ID do tema
    @FXML
    private TextField txtTemaNome;  // Campo de texto para o nome do tema
    @FXML
    private Button btnTemaSalvar, btnTemaAtualizar, btnTemaExcluir, btnTemaLimpar, btnTemaBuscar;

    // Componentes FXML para a tabela que exibe os temas
    @FXML
    private TableView<Tema> tblTemas;     // Tabela para mostrar os temas
    @FXML
    private TableColumn<Tema, Integer> colTemaId;   // Coluna para exibir o ID do tema
    @FXML
    private TableColumn<Tema, String> colTemaNome;    // Coluna para exibir o nome do tema

    // Instância do DAO para operações na base de dados
    private final TemaDAO temaDAO = new TemaDAO();
    // Lista observável que contém os dados dos temas para a TableView
    private final ObservableList<Tema> temaData = FXCollections.observableArrayList();

    /**
     * Método inicializado automaticamente após a injeção dos componentes FXML.
     * Configura a tabela, os eventos associados e carrega os dados iniciais.
     */
    @FXML
    public void initialize() {
        configurarTabela();
        configurarEventosTabela();
        carregarTemas();
        estadoInicial();
    }

    /**
     * Configura a TableView associando as colunas às respetivas propriedades do objeto Tema.
     */
    private void configurarTabela() {
        colTemaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTemaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tblTemas.setItems(temaData);
    }

    /**
     * Configura os eventos da tabela: quando um tema é selecionado,
     * os campos do formulário são automaticamente preenchidos.
     */
    private void configurarEventosTabela() {
        tblTemas.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> preencherCampos(newVal)
        );
    }

    /**
     * Carrega todos os temas da base de dados e atualiza a lista observável.
     */
    private void carregarTemas() {
        temaData.setAll(temaDAO.listarTodos());
    }

    /**
     * Preenche os campos do formulário com os dados do objeto Tema recebido como parâmetro.
     * Também desativa o campo de ID e ajusta o estado dos botões para atualização e exclusão.
     *
     * @param tema O tema selecionado na tabela.
     */
    private void preencherCampos(Tema tema) {
        if (tema != null) {
            txtTemaId.setText(String.valueOf(tema.getId()));
            txtTemaNome.setText(tema.getNome());
            txtTemaId.setDisable(true);      // Impede a modificação manual do ID
            btnTemaSalvar.setDisable(true);    // Desabilita o botão salvar (novo) ao editar
            btnTemaAtualizar.setDisable(false);
            btnTemaExcluir.setDisable(false);
        }
    }

    /**
     * Manipulador do evento de salvar um novo tema.
     * Valida os campos, cria um objeto Tema e invoca o DAO para inserção na base de dados.
     */
    @FXML
    private void handleSalvarTema() {
        String nome = txtTemaNome.getText().trim();

        if (nome.isEmpty()) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro de Validação", "O campo Nome é obrigatório.");
            return;
        }

        // Cria um novo objeto Tema com ID 0, indicando que é um novo registo
        Tema novoTema = new Tema(0, nome);
        if (temaDAO.inserir(novoTema)) {
            sucesso("Tema salvo com sucesso!");
        } else {
            erro("Falha ao salvar o tema.");
        }
    }

    /**
     * Manipulador do evento de atualizar um tema existente.
     * Converte o campo ID para inteiro, valida o campo Nome e invoca o DAO para atualizar o registo.
     */
    @FXML
    private void handleAtualizarTema() {
        try {
            int id = Integer.parseInt(txtTemaId.getText().trim());
            String nome = txtTemaNome.getText().trim();

            if (nome.isEmpty()) {
                exibirAlerta(Alert.AlertType.ERROR, "Erro de Validação", "O campo Nome é obrigatório.");
                return;
            }

            // Cria um objeto Tema com os dados atualizados
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

    /**
     * Manipulador do evento de excluir um tema.
     * Converte o campo ID para inteiro, solicita confirmação ao utilizador e invoca o DAO para exclusão.
     */
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

    /**
     * Manipulador do evento de limpar os campos do formulário.
     * Restaura o estado dos campos para inserção de um novo tema.
     */
    @FXML
    private void handleLimparTema() {
        txtTemaId.clear();
        txtTemaNome.clear();
        txtTemaId.setDisable(false);
        tblTemas.getSelectionModel().clearSelection();
        estadoInicial();
    }

    /**
     * Manipulador do evento de buscar um tema por ID.
     * Converte o campo ID para inteiro e, se encontrado, preenche os campos e seleciona o registo na tabela.
     */
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

    /**
     * Seleciona o tema na TableView que corresponde ao ID fornecido.
     *
     * @param id O ID do tema a selecionar.
     */
    private void selecionarNaTabela(int id) {
        temaData.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .ifPresent(t -> {
                    tblTemas.getSelectionModel().select(t);
                    tblTemas.scrollTo(t);
                });
    }

    /**
     * Define o estado inicial da interface, desabilitando botões de atualização e exclusão,
     * ativando o botão de salvar e posicionando o foco no campo de nome.
     */
    private void estadoInicial() {
        btnTemaSalvar.setDisable(false);
        btnTemaAtualizar.setDisable(true);
        btnTemaExcluir.setDisable(true);
        txtTemaNome.requestFocus();
        carregarTemas();
    }

    /**
     * Exibe uma mensagem de sucesso e reverte a interface ao estado inicial.
     *
     * @param mensagem Mensagem de sucesso a exibir.
     */
    private void sucesso(String mensagem) {
        exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", mensagem);
        estadoInicial();
        handleLimparTema();
    }

    /**
     * Exibe uma mensagem de erro.
     *
     * @param mensagem Mensagem de erro a exibir.
     */
    private void erro(String mensagem) {
        exibirAlerta(Alert.AlertType.ERROR, "Erro", mensagem);
    }

    /**
     * Exibe uma caixa de diálogo para confirmação e retorna true se o usuário confirmar.
     *
     * @param mensagem Mensagem de confirmação.
     * @return true se o usuário confirmar; false caso contrário.
     */
    private boolean confirmar(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, mensagem, ButtonType.YES, ButtonType.NO);
        alerta.setTitle("Confirmação");
        alerta.setHeaderText(null);
        Optional<ButtonType> resultado = alerta.showAndWait();
        return resultado.isPresent() && resultado.get() == ButtonType.YES;
    }

    /**
     * Exibe um alerta com base no tipo, título e mensagem especificados.
     *
     * @param tipo     Tipo de alerta (INFORMATION, ERROR, etc.)
     * @param titulo   Título do alerta
     * @param mensagem Mensagem a ser exibida
     */
    private void exibirAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
