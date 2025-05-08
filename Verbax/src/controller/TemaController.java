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

    @FXML private Button btnTemaSalvar;
    @FXML private Button btnTemaAtualizar;
    @FXML private Button btnTemaExcluir;
    @FXML private Button btnTemaLimpar;
    @FXML private Button btnTemaBuscar;

    @FXML private TableView<Tema> tblTemas;
    @FXML private TableColumn<Tema, Integer> colTemaId;
    @FXML private TableColumn<Tema, String> colTemaNome;

    private TemaDAO temaDAO;
    private ObservableList<Tema> temaData;

    @FXML
    public void initialize() {
        temaDAO = new TemaDAO();

        // Configura as colunas da tabela
        colTemaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTemaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        temaData = FXCollections.observableArrayList();
        tblTemas.setItems(temaData);

        // Adiciona um listener para a seleção de itens na tabela
        tblTemas.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> preencherCampos(newValue));

        // Carrega os dados iniciais na tabela
        carregarTemas();

        // Define o estado inicial dos botões
        btnTemaAtualizar.setDisable(true);
        btnTemaExcluir.setDisable(true);
    }

    private void carregarTemas() {
        temaData.clear();
        List<Tema> temas = temaDAO.listarTodos();
        temaData.addAll(temas);
        tblTemas.refresh(); // Garante que a tabela seja atualizada visualmente
    }

    private void preencherCampos(Tema tema) {
        if (tema != null) {
            txtTemaId.setText(String.valueOf(tema.getId()));
            txtTemaNome.setText(tema.getNome());

            txtTemaId.setDisable(true); // ID não deve ser editado diretamente após seleção/busca
            btnTemaSalvar.setDisable(true); // Evita salvar duplicado se os campos já estiverem preenchidos com um item existente
            btnTemaAtualizar.setDisable(false); // Permite atualizar
            btnTemaExcluir.setDisable(false); // Permite excluir
        } else {
            handleLimparTema(); // Chama limpar se nada for selecionado (ou desselecionado)
        }
    }

    @FXML
    private void handleSalvarTema() {
        String nome = txtTemaNome.getText();

        if (nome.isEmpty()) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro de Validação", "O campo Nome é obrigatório.");
            return;
        }

        // ID é gerado pelo banco de dados.
        Tema novoTema = new Tema(0, nome);
        if (temaDAO.inserir(novoTema)) {
            exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Tema salvo com sucesso!");
            carregarTemas();
            handleLimparTema();
        } else {
            exibirAlerta(Alert.AlertType.ERROR, "Erro ao Salvar", "Falha ao salvar o tema no banco de dados.");
        }
    }

    @FXML
    private void handleAtualizarTema() {
        String idStr = txtTemaId.getText();
        String nome = txtTemaNome.getText();

        if (idStr.isEmpty() || nome.isEmpty()) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro de Validação", "Os campos ID e Nome são obrigatórios para atualização.");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            Tema tema = new Tema(id, nome);
            if (temaDAO.atualizar(tema)) {
                exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Tema atualizado com sucesso!");
                carregarTemas();
                handleLimparTema();
            } else {
                exibirAlerta(Alert.AlertType.ERROR, "Erro ao Atualizar", "Falha ao atualizar o tema. Verifique se o ID é válido e existe.");
            }
        } catch (NumberFormatException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro de Formato", "O ID do tema deve ser um número válido.");
        }
    }

    @FXML
    private void handleExcluirTema() {
        String idStr = txtTemaId.getText();
        Tema selecionado = tblTemas.getSelectionModel().getSelectedItem();

        if (idStr.isEmpty() && selecionado == null) {
            exibirAlerta(Alert.AlertType.WARNING, "Nenhuma Seleção", "Por favor, digite um ID ou selecione um tema na tabela para excluir.");
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
        } else { // selecionado != null
            idParaExcluir = selecionado.getId();
        }

        Alert alertConfirmacao = new Alert(Alert.AlertType.CONFIRMATION,
                "Tem certeza que deseja excluir o tema com ID " + idParaExcluir + "?",
                ButtonType.YES, ButtonType.NO);
        alertConfirmacao.setTitle("Confirmar Exclusão");
        alertConfirmacao.setHeaderText(null);
        Optional<ButtonType> resultado = alertConfirmacao.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.YES) {
            if (temaDAO.excluir(idParaExcluir)) {
                exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Tema excluído com sucesso!");
                carregarTemas();
                handleLimparTema();
            } else {
                exibirAlerta(Alert.AlertType.ERROR, "Erro ao Excluir", "Falha ao excluir o tema. Verifique se o ID é válido, existe e não está em uso por outra entidade (ex: Livro).");
            }
        }
    }

    @FXML
    private void handleLimparTema() {
        txtTemaId.clear();
        txtTemaNome.clear();
        tblTemas.getSelectionModel().clearSelection();

        txtTemaId.setDisable(false); // Permite digitar ID para busca
        btnTemaSalvar.setDisable(false); // Permite salvar
        btnTemaAtualizar.setDisable(true); // Desabilita atualizar ao limpar
        btnTemaExcluir.setDisable(true); // Desabilita excluir ao limpar

        txtTemaNome.requestFocus(); // Foco no campo nome para nova entrada
    }

    @FXML
    private void handleBuscarTemaPorId() {
        String idStr = txtTemaId.getText();
        if (idStr.isEmpty()) {
            exibirAlerta(Alert.AlertType.WARNING, "Campo Vazio", "Por favor, insira um ID para buscar.");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            Tema tema = temaDAO.buscarPorId(id);
            if (tema != null) {
                preencherCampos(tema); // Isso também desabilitará o campo ID e ajustará botões

                // Seleciona e foca o item na tabela (se existir)
                tblTemas.getItems().stream()
                        .filter(item -> item.getId() == id)
                        .findFirst()
                        .ifPresent(item -> {
                            tblTemas.getSelectionModel().select(item);
                            tblTemas.scrollTo(item);
                        });
            } else {
                exibirAlerta(Alert.AlertType.INFORMATION, "Não Encontrado", "Tema com ID " + id + " não encontrado.");
                // Limpa o campo de nome, mas mantém o ID digitado.
                txtTemaNome.clear();
                txtTemaId.setDisable(false); // Permite editar o ID para nova busca
                btnTemaSalvar.setDisable(false);
                btnTemaAtualizar.setDisable(true);
                btnTemaExcluir.setDisable(true);
            }
        } catch (NumberFormatException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro de Formato", "O ID do tema deve ser um número válido.");
        }
    }

    private void exibirAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}