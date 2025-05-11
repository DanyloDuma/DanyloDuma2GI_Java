package controller;

// Importações necessárias para a aplicação JavaFX
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.*;
import dao.*;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.control.TableCell;

public class LivroController implements Initializable {

    // Campos do formulário definidos no FXML
    @FXML
    private TextField txtLivroId;
    @FXML
    private TextField txtLivroTitulo;
    @FXML
    private TextField txtLivroAnoPublicacao;
    @FXML
    private TextField txtLivroIsbn;
    @FXML
    private ComboBox<Autor> cmbLivroAutor;
    @FXML
    private ComboBox<Tema> cmbLivroTema;
    @FXML
    private ComboBox<Editora> cmbLivroEditora;
    @FXML
    private ComboBox<Localizacao> cmbLivroLocalizacao;

    @FXML
    private Button btnLivroSalvar;
    @FXML
    private Button btnLivroAtualizar;
    @FXML
    private Button btnLivroExcluir;
    @FXML
    private Button btnLivroLimpar;
    @FXML
    private Button btnLivroBuscar;

    // Botões para abrir janelas de gestão de entidades relacionadas
    @FXML
    private Button btnGerenciarAutor;
    @FXML
    private Button btnGerenciarTema;
    @FXML
    private Button btnGerenciarEditora;
    @FXML
    private Button btnGerenciarLocalizacao;

    // Tabela e colunas para exibir os livros
    @FXML
    private TableView<Livro> tblLivros;
    @FXML
    private TableColumn<Livro, Integer> colLivroId;
    @FXML
    private TableColumn<Livro, String> colLivroTitulo;
    @FXML
    private TableColumn<Livro, Integer> colLivroAnoPublicacao;
    @FXML
    private TableColumn<Livro, String> colLivroIsbn;
    @FXML
    private TableColumn<Livro, Autor> colLivroAutor;
    @FXML
    private TableColumn<Livro, Tema> colLivroTema;
    @FXML
    private TableColumn<Livro, Editora> colLivroEditora;
    @FXML
    private TableColumn<Livro, Localizacao> colLivroLocalizacao;

    // DAOs para acesso à base de dados
    private LivroDAO livroDAO;
    private AutorDAO autorDAO;
    private TemaDAO temaDAO;
    private EditoraDAO editoraDAO;
    private LocalizacaoDAO localizacaoDAO;

    // Listas observáveis para alimentar a interface
    private ObservableList<Livro> listaLivros;
    private ObservableList<Autor> listaAutores;
    private ObservableList<Tema> listaTemas;
    private ObservableList<Editora> listaEditoras;
    private ObservableList<Localizacao> listaLocalizacoes;

    // Inicializa o controlador
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Instancia os DAOs
        livroDAO = new LivroDAO();
        autorDAO = new AutorDAO();
        temaDAO = new TemaDAO();
        editoraDAO = new EditoraDAO();
        localizacaoDAO = new LocalizacaoDAO();

        // Define como os dados são exibidos nas colunas da tabela
        colLivroId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colLivroTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colLivroAnoPublicacao.setCellValueFactory(new PropertyValueFactory<>("anoPublicacao"));
        colLivroIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colLivroAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colLivroTema.setCellValueFactory(new PropertyValueFactory<>("tema"));
        colLivroEditora.setCellValueFactory(new PropertyValueFactory<>("editora"));
        colLivroLocalizacao.setCellValueFactory(new PropertyValueFactory<>("localizacao"));

        // Configura como os objetos são apresentados nas células da tabela
        configureCellFactories();

        // Carrega os dados nas ComboBoxes
        carregarComboBoxes();

        // Carrega os livros na tabela
        carregarTableViewLivros();

        // Adiciona um ouvinte para quando o utilizador selecionar um livro na tabela
        tblLivros.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarLivro(newValue));
    }

    // Configura as células da tabela para exibir texto amigável
    private void configureCellFactories() {
        colLivroAutor.setCellFactory(column -> new TableCell<Livro, Autor>() {
            @Override
            protected void updateItem(Autor item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNome());
            }
        });
        colLivroTema.setCellFactory(column -> new TableCell<Livro, Tema>() {
            @Override
            protected void updateItem(Tema item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNome());
            }
        });
        colLivroEditora.setCellFactory(column -> new TableCell<Livro, Editora>() {
            @Override
            protected void updateItem(Editora item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNome());
            }
        });
        colLivroLocalizacao.setCellFactory(column -> new TableCell<Livro, Localizacao>() {
            @Override
            protected void updateItem(Localizacao item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getSetor() + " - " + item.getPrateleira());
            }
        });
    }

    // Carrega todas as ComboBoxes com dados da base de dados
    private void carregarComboBoxes() {
        carregarComboBoxAutores();
        carregarComboBoxTemas();
        carregarComboBoxEditoras();
        carregarComboBoxLocalizacoes();
    }

    // Carrega autores e define como exibir os nomes na ComboBox
    private void carregarComboBoxAutores() {
        listaAutores = FXCollections.observableArrayList(autorDAO.listarTodos());
        cmbLivroAutor.setItems(listaAutores);
        cmbLivroAutor.setConverter(new StringConverter<Autor>() {
            @Override
            public String toString(Autor autor) {
                return autor == null ? "" : autor.getNome();
            }

            @Override
            public Autor fromString(String string) {
                return null;
            }
        });
    }

    // Carrega temas e define como exibir os nomes na ComboBox
    private void carregarComboBoxTemas() {
        listaTemas = FXCollections.observableArrayList(temaDAO.listarTodos());
        cmbLivroTema.setItems(listaTemas);
        cmbLivroTema.setConverter(new StringConverter<Tema>() {
            @Override
            public String toString(Tema tema) {
                return tema == null ? "" : tema.getNome();
            }

            @Override
            public Tema fromString(String string) {
                return null;
            }
        });
    }

    // Carrega editoras e define como exibir os nomes na ComboBox
    private void carregarComboBoxEditoras() {
        listaEditoras = FXCollections.observableArrayList(editoraDAO.listarTodos());
        cmbLivroEditora.setItems(listaEditoras);
        cmbLivroEditora.setConverter(new StringConverter<Editora>() {
            @Override
            public String toString(Editora editora) {
                return editora == null ? "" : editora.getNome();
            }

            @Override
            public Editora fromString(String string) {
                return null;
            }
        });
    }

    // Carrega localizações e define como exibir na ComboBox
    private void carregarComboBoxLocalizacoes() {
        listaLocalizacoes = FXCollections.observableArrayList(localizacaoDAO.listarTodos());
        cmbLivroLocalizacao.setItems(listaLocalizacoes);
        cmbLivroLocalizacao.setConverter(new StringConverter<Localizacao>() {
            @Override
            public String toString(Localizacao localizacao) {
                return localizacao == null ? "" : localizacao.getSetor() + " - " + localizacao.getPrateleira();
            }

            @Override
            public Localizacao fromString(String string) {
                return null;
            }
        });
    }

    // Carrega todos os livros na tabela
    private void carregarTableViewLivros() {
        listaLivros = FXCollections.observableArrayList(livroDAO.listarTodos());
        tblLivros.setItems(listaLivros);
        // tblLivros.refresh(); // Pode ser usado para forçar atualização
    }

    // Preenche os campos do formulário com os dados do livro selecionado
    private void selecionarLivro(Livro livro) {
        if (livro != null) {
            txtLivroId.setText(String.valueOf(livro.getId()));
            txtLivroTitulo.setText(livro.getTitulo());
            txtLivroAnoPublicacao.setText(String.valueOf(livro.getAnoPublicacao()));
            txtLivroIsbn.setText(livro.getIsbn());
            cmbLivroAutor.setValue(livro.getAutor());
            cmbLivroTema.setValue(livro.getTema());
            cmbLivroEditora.setValue(livro.getEditora());
            cmbLivroLocalizacao.setValue(livro.getLocalizacao());
        } else {
            limparCampos();
        }
    }

    // Trata o clique no botão "Salvar"
    @FXML
    void handleSalvarLivro(ActionEvent event) {
        if (validarCampos()) {
            Livro livro = new Livro();
            livro.setTitulo(txtLivroTitulo.getText());
            livro.setAnoPublicacao(Integer.parseInt(txtLivroAnoPublicacao.getText()));
            livro.setIsbn(txtLivroIsbn.getText());
            livro.setAutor(cmbLivroAutor.getValue());
            livro.setTema(cmbLivroTema.getValue());
            livro.setEditora(cmbLivroEditora.getValue());
            livro.setLocalizacao(cmbLivroLocalizacao.getValue());

            boolean inserido = livroDAO.inserir(livro);
            if (inserido) {
                mostrarMensagem(Alert.AlertType.INFORMATION, "Sucesso", "Livro salvo com sucesso!");
                limparCampos();
                carregarTableViewLivros();
            } else {
                mostrarMensagem(Alert.AlertType.ERROR, "Erro", "Erro ao salvar livro.");
            }
        }
    }

    // Trata o clique no botão "Atualizar"
    @FXML
    void handleAtualizarLivro(ActionEvent event) {
        if (txtLivroId.getText().isEmpty()) {
            mostrarMensagem(Alert.AlertType.WARNING, "Aviso", "Selecione um livro na tabela ou informe o ID para atualizar.");
            return;
        }
        if (validarCampos()) {
            try {
                int id = Integer.parseInt(txtLivroId.getText());
                Livro livro = new Livro();
                livro.setId(id);
                livro.setTitulo(txtLivroTitulo.getText());
                livro.setAnoPublicacao(Integer.parseInt(txtLivroAnoPublicacao.getText()));
                livro.setIsbn(txtLivroIsbn.getText());
                livro.setAutor(cmbLivroAutor.getValue());
                livro.setTema(cmbLivroTema.getValue());
                livro.setEditora(cmbLivroEditora.getValue());
                livro.setLocalizacao(cmbLivroLocalizacao.getValue());

                boolean atualizado = livroDAO.atualizar(livro);
                if (atualizado) {
                    mostrarMensagem(Alert.AlertType.INFORMATION, "Sucesso", "Livro atualizado com sucesso!");
                    limparCampos();
                    carregarTableViewLivros();
                } else {
                    mostrarMensagem(Alert.AlertType.ERROR, "Erro", "Erro ao atualizar livro.");
                }
            } catch (NumberFormatException e) {
                mostrarMensagem(Alert.AlertType.ERROR, "Erro de Formato", "ID e Ano de Publicação devem ser números inteiros.");
            }
        }
    }

    // Trata o clique no botão "Excluir"
    @FXML
    void handleExcluirLivro(ActionEvent event) {
        if (txtLivroId.getText().isEmpty()) {
            mostrarMensagem(Alert.AlertType.WARNING, "Aviso", "Selecione um livro na tabela ou informe o ID para excluir.");
            return;
        }
        try {
            int id = Integer.parseInt(txtLivroId.getText());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Tem certeza que deseja excluir este livro?", ButtonType.YES, ButtonType.NO);
            alert.setTitle("Confirmação de Exclusão");
            alert.setHeaderText(null);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                boolean excluido = livroDAO.excluir(id);
                if (excluido) {
                    mostrarMensagem(Alert.AlertType.INFORMATION, "Sucesso", "Livro excluído com sucesso!");
                    limparCampos();
                    carregarTableViewLivros();
                } else {
                    mostrarMensagem(Alert.AlertType.ERROR, "Erro", "Erro ao excluir livro.");
                }
            }
        } catch (NumberFormatException e) {
            mostrarMensagem(Alert.AlertType.ERROR, "Erro de Formato", "ID deve ser um número inteiro.");
        }
    }

    // Trata o clique no botão "Limpar"
    @FXML
    void handleLimparLivro(ActionEvent event) {
        limparCampos();
    }

    // Trata o clique no botão "Buscar"
    @FXML
    void handleBuscarLivroPorId(ActionEvent event) {
        if (txtLivroId.getText().isEmpty()) {
            mostrarMensagem(Alert.AlertType.WARNING, "Aviso", "Informe o ID do livro para buscar.");
            return;
        }
        try {
            int id = Integer.parseInt(txtLivroId.getText());
            Livro livro = livroDAO.buscarPorId(id);
            if (livro != null) {
                selecionarLivro(livro);
            } else {
                mostrarMensagem(Alert.AlertType.INFORMATION, "Não Encontrado", "Livro com ID " + id + " não encontrado.");
                limparCampos();
            }
        } catch (NumberFormatException e) {
            mostrarMensagem(Alert.AlertType.ERROR, "Erro de Formato", "ID deve ser um número inteiro.");
        }
    }

    // Limpa todos os campos do formulário
    private void limparCampos() {
        txtLivroId.clear();
        txtLivroTitulo.clear();
        txtLivroAnoPublicacao.clear();
        txtLivroIsbn.clear();
        cmbLivroAutor.setValue(null);
        cmbLivroTema.setValue(null);
        cmbLivroEditora.setValue(null);
        cmbLivroLocalizacao.setValue(null);
        tblLivros.getSelectionModel().clearSelection();
    }

    // Valida se todos os campos obrigatórios foram preenchidos corretamente
    private boolean validarCampos() {
        String titulo = txtLivroTitulo.getText();
        String anoPublicacao = txtLivroAnoPublicacao.getText();
        String isbn = txtLivroIsbn.getText();
        Autor autor = cmbLivroAutor.getValue();
        Tema tema = cmbLivroTema.getValue();
        Editora editora = cmbLivroEditora.getValue();
        Localizacao localizacao = cmbLivroLocalizacao.getValue();

        if (titulo.isEmpty() || anoPublicacao.isEmpty() || isbn.isEmpty() || autor == null || tema == null || editora == null || localizacao == null) {
            mostrarMensagem(Alert.AlertType.WARNING, "Campos Incompletos", "Por favor, preencha todos os campos e selecione as opções.");
            return false;
        }
        try {
            Integer.parseInt(anoPublicacao);
        } catch (NumberFormatException e) {
            mostrarMensagem(Alert.AlertType.ERROR, "Erro de Formato", "Ano de Publicação deve ser um número inteiro.");
            return false;
        }
        return true;
    }

    // Exibe uma mensagem de alerta na interface
    private void mostrarMensagem(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Método genérico para abrir janelas de gestão de entidades
    private void abrirJanelaGerenciamento(String fxmlPath, String title, Runnable callbackAtualizacao) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            if (callbackAtualizacao != null) {
                callbackAtualizacao.run();
            }
        } catch (IOException e) {
            e.printStackTrace();
            mostrarMensagem(Alert.AlertType.ERROR, "Erro ao Abrir Janela", "Não foi possível abrir a tela de " + title.toLowerCase() + ".");
        }
    }

    // Ações dos botões para abrir janelas de gestão de entidades relacionadas
    @FXML
    void handleGerenciarAutor(ActionEvent event) {
        abrirJanelaGerenciamento("/view/AutorView.fxml", "Gerenciar Autores", this::carregarComboBoxAutores);
    }

    @FXML
    void handleGerenciarTema(ActionEvent event) {
        abrirJanelaGerenciamento("/view/TemaView.fxml", "Gerenciar Temas", this::carregarComboBoxTemas);
    }

    @FXML
    void handleGerenciarEditora(ActionEvent event) {
        abrirJanelaGerenciamento("/view/EditoraView.fxml", "Gerenciar Editoras", this::carregarComboBoxEditoras);
    }

    @FXML
    void handleGerenciarLocalizacao(ActionEvent event) {
        abrirJanelaGerenciamento("/view/LocalizacaoView.fxml", "Gerenciar Localizações", this::carregarComboBoxLocalizacoes);
    }
}
