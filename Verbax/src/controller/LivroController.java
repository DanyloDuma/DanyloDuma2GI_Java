package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType; // Importar ButtonType
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import model.*; // Importar todas as classes do pacote model
import dao.*; // Importar todas as classes do pacote dao

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.control.TableCell; // Importar TableCell

public class LivroController implements Initializable {

    // Componentes da interface de utilizador definidos no FXML
    @FXML private TextField txtLivroId;
    @FXML private TextField txtLivroTitulo;
    @FXML private TextField txtLivroAnoPublicacao;
    @FXML private TextField txtLivroIsbn;
    @FXML private ComboBox<Autor> cmbLivroAutor;
    @FXML private ComboBox<Tema> cmbLivroTema;
    @FXML private ComboBox<Editora> cmbLivroEditora;
    @FXML private ComboBox<Localizacao> cmbLivroLocalizacao;

    @FXML private Button btnLivroSalvar;
    @FXML private Button btnLivroAtualizar;
    @FXML private Button btnLivroExcluir;
    @FXML private Button btnLivroLimpar;
    @FXML private Button btnLivroBuscar;

    @FXML private TableView<Livro> tblLivros;
    @FXML private TableColumn<Livro, Integer> colLivroId;
    @FXML private TableColumn<Livro, String> colLivroTitulo;
    @FXML private TableColumn<Livro, Integer> colLivroAnoPublicacao;
    @FXML private TableColumn<Livro, String> colLivroIsbn;
    @FXML private TableColumn<Livro, Autor> colLivroAutor;
    @FXML private TableColumn<Livro, Tema> colLivroTema;
    @FXML private TableColumn<Livro, Editora> colLivroEditora;
    @FXML private TableColumn<Livro, Localizacao> colLivroLocalizacao;

    // Instâncias dos Data Access Objects para interagir com a base de dados
    private LivroDAO livroDAO;
    private AutorDAO autorDAO;
    private TemaDAO temaDAO;
    private EditoraDAO editoraDAO;
    private LocalizacaoDAO localizacaoDAO;

    // Listas observáveis para preencher a TableView e os ComboBoxes
    private ObservableList<Livro> listaLivros;
    private ObservableList<Autor> listaAutores;
    private ObservableList<Tema> listaTemas;
    private ObservableList<Editora> listaEditoras;
    private ObservableList<Localizacao> listaLocalizacoes;

    // Método chamado automaticamente após o carregamento do FXML
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializa os DAOs
        livroDAO = new LivroDAO();
        autorDAO = new AutorDAO();
        temaDAO = new TemaDAO();
        editoraDAO = new EditoraDAO();
        localizacaoDAO = new LocalizacaoDAO();

        // Configura as colunas da TableView para mapear para as propriedades do modelo Livro
        colLivroId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colLivroTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colLivroAnoPublicacao.setCellValueFactory(new PropertyValueFactory<>("anoPublicacao"));
        colLivroIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        // Para objetos relacionados, use PropertyValueFactory apontando para o nome da propriedade
        // e garanta que a classe do modelo tenha getters para essas propriedades (o que tem)
        colLivroAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colLivroTema.setCellValueFactory(new PropertyValueFactory<>("tema"));
        colLivroEditora.setCellValueFactory(new PropertyValueFactory<>("editora"));
        colLivroLocalizacao.setCellValueFactory(new PropertyValueFactory<>("localizacao"));

        // Define Cell Factories personalizados para as colunas de objetos relacionados
        // para exibir informações significativas em vez da referência do objeto
        colLivroAutor.setCellFactory(column -> new TableCell<Livro, Autor>() {
            @Override
            protected void updateItem(Autor item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNome()); // Exibe o nome do Autor
            }
        });
        colLivroTema.setCellFactory(column -> new TableCell<Livro, Tema>() {
            @Override
            protected void updateItem(Tema item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNome()); // Exibe o nome do Tema
            }
        });
        colLivroEditora.setCellFactory(column -> new TableCell<Livro, Editora>() {
            @Override
            protected void updateItem(Editora item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNome()); // Exibe o nome da Editora
            }
        });
        colLivroLocalizacao.setCellFactory(column -> new TableCell<Livro, Localizacao>() {
            @Override
            protected void updateItem(Localizacao item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getSetor() + " - " + item.getPrateleira()); // Exibe detalhes da Localização
            }
        });


        // Carrega os dados para os ComboBoxes
        carregarComboBoxes();

        // Carrega os dados iniciais para a TableView
        carregarTableViewLivros();

        // Adiciona um listener à seleção da TableView para preencher o formulário
        tblLivros.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarLivro(newValue));
    }

    // Método para carregar dados nos ComboBoxes
    private void carregarComboBoxes() {
        listaAutores = FXCollections.observableArrayList(autorDAO.listarTodos());
        cmbLivroAutor.setItems(listaAutores);
        // Configura um StringConverter para o ComboBox de Autor para exibir o nome
        cmbLivroAutor.setConverter(new StringConverter<Autor>() {
            @Override
            public String toString(Autor autor) {
                return autor == null ? "" : autor.getNome();
            }

            @Override
            public Autor fromString(String string) {
                // Não é necessário para este caso de uso (selecionar de uma lista)
                return null;
            }
        });


        listaTemas = FXCollections.observableArrayList(temaDAO.listarTodos());
        cmbLivroTema.setItems(listaTemas);
        // Configura um StringConverter para o ComboBox de Tema
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

        listaEditoras = FXCollections.observableArrayList(editoraDAO.listarTodos());
        cmbLivroEditora.setItems(listaEditoras);
        // Configura um StringConverter para o ComboBox de Editora
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

        listaLocalizacoes = FXCollections.observableArrayList(localizacaoDAO.listarTodos());
        cmbLivroLocalizacao.setItems(listaLocalizacoes);
        // Configura um StringConverter para o ComboBox de Localização
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

    // Método para carregar dados na TableView de Livros
    private void carregarTableViewLivros() {
        // ESTE MÉTODO CHAMA O DAO QUE USA DBConnection.getConnection()
        // E DEVIDO À IMPLEMENTAÇÃO ATUAL DE DBConnection E AO USO DE
        // try-with-resources NOS DAOs, A LIGAÇÃO SERÁ FECHADA APÓS ESTA OPERAÇÃO.
        // OPERAÇÕES SUBSEQUENTES VIA DAOs FALHARÃO.
        listaLivros = FXCollections.observableArrayList(livroDAO.listarTodos());
        tblLivros.setItems(listaLivros);
    }

    // Método para preencher os campos do formulário com dados de um livro selecionado
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

    // Manipulador do evento do botão Salvar
    @FXML
    void handleSalvarLivro(ActionEvent event) {
        if (validarCampos()) {
            Livro livro = new Livro();
            livro.setTitulo(txtLivroTitulo.getText());
            // É necessário converter para Integer; validarCampos já verifica o formato
            livro.setAnoPublicacao(Integer.parseInt(txtLivroAnoPublicacao.getText()));
            livro.setIsbn(txtLivroIsbn.getText());
            livro.setAutor(cmbLivroAutor.getValue());
            livro.setTema(cmbLivroTema.getValue());
            livro.setEditora(cmbLivroEditora.getValue());
            livro.setLocalizacao(cmbLivroLocalizacao.getValue());

            // ESTA OPERAÇÃO CHAMARÁ O DAO QUE USA A LIGAÇÃO. SE ESTA NÃO FOR A PRIMEIRA
            // OPERAÇÃO APÓS O INÍCIO DA APP, PROVAVELMENTE FALHARÁ DEVIDO À LIGAÇÃO FECHADA.
            boolean inserido = livroDAO.inserir(livro);

            if (inserido) {
                mostrarMensagem(Alert.AlertType.INFORMATION, "Sucesso", "Livro salvo com sucesso!");
                limparCampos();
                // Recarregar a tabela provavelmente causará uma falha de ligação
                carregarTableViewLivros();
            } else {
                mostrarMensagem(Alert.AlertType.ERROR, "Erro", "Erro ao salvar livro. Verifique o log (e a ligação à base de dados).");
            }
        }
    }

    // Manipulador do evento do botão Atualizar
    @FXML
    void handleAtualizarLivro(ActionEvent event) {
        // Verifica se o campo ID está preenchido antes de tentar atualizar
        if (txtLivroId.getText().isEmpty()) {
            mostrarMensagem(Alert.AlertType.WARNING, "Aviso", "Selecione um livro na tabela ou informe o ID para atualizar.");
            return;
        }

        if (validarCampos()) {
            try {
                // É necessário converter para Integer
                int id = Integer.parseInt(txtLivroId.getText());
                Livro livro = new Livro();
                livro.setId(id);
                livro.setTitulo(txtLivroTitulo.getText());
                // É necessário converter para Integer; validarCampos já verifica o formato
                livro.setAnoPublicacao(Integer.parseInt(txtLivroAnoPublicacao.getText()));
                livro.setIsbn(txtLivroIsbn.getText());
                livro.setAutor(cmbLivroAutor.getValue());
                livro.setTema(cmbLivroTema.getValue());
                livro.setEditora(cmbLivroEditora.getValue());
                livro.setLocalizacao(cmbLivroLocalizacao.getValue());

                // ESTA OPERAÇÃO PROVAVELMENTE FALHARÁ DEVIDO À LIGAÇÃO FECHADA.
                boolean atualizado = livroDAO.atualizar(livro);

                if (atualizado) {
                    mostrarMensagem(Alert.AlertType.INFORMATION, "Sucesso", "Livro atualizado com sucesso!");
                    limparCampos();
                    // Recarregar a tabela provavelmente causará uma falha de ligação
                    carregarTableViewLivros();
                } else {
                    mostrarMensagem(Alert.AlertType.ERROR, "Erro", "Erro ao atualizar livro. Verifique o log (e a ligação à base de dados).");
                }
            } catch (NumberFormatException e) {
                // Captura erro se o ID ou Ano de Publicação não for um número
                mostrarMensagem(Alert.AlertType.ERROR, "Erro de Formato", "ID e Ano de Publicação devem ser números inteiros.");
            }
        }
    }

    // Manipulador do evento do botão Excluir
    @FXML
    void handleExcluirLivro(ActionEvent event) {
        // Verifica se o campo ID está preenchido antes de tentar excluir
        if (txtLivroId.getText().isEmpty()) {
            mostrarMensagem(Alert.AlertType.WARNING, "Aviso", "Selecione um livro na tabela ou informe o ID para excluir.");
            return;
        }

        try {
            // É necessário converter para Integer
            int id = Integer.parseInt(txtLivroId.getText());
            // Opcional: Adicionar um diálogo de confirmação
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Tem certeza que deseja excluir este livro?", ButtonType.YES, ButtonType.NO);
            alert.setTitle("Confirmação de Exclusão");
            alert.setHeaderText(null); // Remove o cabeçalho padrão
            alert.showAndWait(); // Espera pela resposta do utilizador

            // Se o utilizador confirmar (clicar em SIM)
            if (alert.getResult() == ButtonType.YES) {
                // ESTA OPERAÇÃO PROVAVELMENTE FALHARÁ DEVIDO À LIGAÇÃO FECHADA.
                boolean excluido = livroDAO.excluir(id);

                if (excluido) {
                    mostrarMensagem(Alert.AlertType.INFORMATION, "Sucesso", "Livro excluído com sucesso!");
                    limparCampos();
                    // Recarregar a tabela provavelmente causará uma falha de ligação
                    carregarTableViewLivros();
                } else {
                    mostrarMensagem(Alert.AlertType.ERROR, "Erro", "Erro ao excluir livro. Verifique o log (e a ligação à base de dados).");
                }
            }
        } catch (NumberFormatException e) {
            // Captura erro se o ID não for um número
            mostrarMensagem(Alert.AlertType.ERROR, "Erro de Formato", "ID deve ser um número inteiro.");
        }
    }

    // Manipulador do evento do botão Limpar Campos
    @FXML
    void handleLimparLivro(ActionEvent event) {
        limparCampos(); // Chama o método auxiliar para limpar os campos
    }

    // Manipulador do evento do botão Buscar por ID
    @FXML
    void handleBuscarLivroPorId(ActionEvent event) {
        // Verifica se o campo ID está preenchido antes de tentar buscar
        if (txtLivroId.getText().isEmpty()) {
            mostrarMensagem(Alert.AlertType.WARNING, "Aviso", "Informe o ID do livro para buscar.");
            return;
        }

        try {
            // É necessário converter para Integer
            int id = Integer.parseInt(txtLivroId.getText());
            // ESTA OPERAÇÃO PROVAVELMENTE FALHARÁ DEVIDO À LIGAÇÃO FECHADA.
            Livro livro = livroDAO.buscarPorId(id);

            if (livro != null) {
                selecionarLivro(livro); // Preenche os campos com os dados encontrados
                // Opcional: Destacar na tabela, se o livro estiver visível
            } else {
                mostrarMensagem(Alert.AlertType.INFORMATION, "Não Encontrado", "Livro com ID " + id + " não encontrado.");
                limparCampos(); // Limpa os campos se o livro não for encontrado
            }
        } catch (NumberFormatException e) {
            // Captura erro se o ID não for um número
            mostrarMensagem(Alert.AlertType.ERROR, "Erro de Formato", "ID deve ser um número inteiro.");
        }
    }

    // Método auxiliar para limpar todos os campos do formulário
    private void limparCampos() {
        txtLivroId.clear();
        txtLivroTitulo.clear();
        txtLivroAnoPublicacao.clear();
        txtLivroIsbn.clear();
        cmbLivroAutor.setValue(null); // Define o valor selecionado como nulo
        cmbLivroTema.setValue(null);
        cmbLivroEditora.setValue(null);
        cmbLivroLocalizacao.setValue(null);
        tblLivros.getSelectionModel().clearSelection(); // Limpa a seleção na tabela
    }

    // Método auxiliar para validar os campos do formulário antes de salvar ou atualizar
    private boolean validarCampos() {
        String titulo = txtLivroTitulo.getText();
        String anoPublicacao = txtLivroAnoPublicacao.getText();
        String isbn = txtLivroIsbn.getText();
        Autor autor = cmbLivroAutor.getValue();
        Tema tema = cmbLivroTema.getValue();
        Editora editora = cmbLivroEditora.getValue();
        Localizacao localizacao = cmbLivroLocalizacao.getValue();

        // Verifica se algum campo obrigatório está vazio ou se um item não foi selecionado nos ComboBoxes
        if (titulo.isEmpty() || anoPublicacao.isEmpty() || isbn.isEmpty() || autor == null || tema == null || editora == null || localizacao == null) {
            mostrarMensagem(Alert.AlertType.WARNING, "Campos Incompletos", "Por favor, preencha todos os campos e selecione as opções.");
            return false;
        }

        // Valida se o Ano de Publicação é um número inteiro válido
        try {
            // Tenta converter para Integer
            Integer.parseInt(anoPublicacao);
        } catch (NumberFormatException e) {
            // Se falhar, não é um número válido
            mostrarMensagem(Alert.AlertType.ERROR, "Erro de Formato", "Ano de Publicação deve ser um número inteiro.");
            return false;
        }

        return true; // Retorna true se todos os campos forem válidos
    }

    // Método auxiliar para exibir mensagens ao utilizador
    private void mostrarMensagem(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type); // Cria um novo diálogo de alerta do tipo especificado
        alert.setTitle(title); // Define o título do diálogo
        alert.setHeaderText(null); // Remove o cabeçalho padrão
        alert.setContentText(message); // Define o conteúdo da mensagem
        alert.showAndWait(); // Exibe o diálogo e espera que o utilizador o feche
    }
}