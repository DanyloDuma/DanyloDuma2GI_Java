package application; // Pode ajustar o nome do pacote se for diferente

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.DBConnection; // Importa a classe DBConnection para poder fechá-la ao sair

import java.net.URL; // Importar a classe URL para verificar se o recurso foi encontrado

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Carrega o ficheiro FXML da view principal (neste caso, a de Livros)
            // O caminho correto é /application/view/LivroView.fxml com base na estrutura do projeto e no classpath
            URL fxmlLocation = getClass().getResource("/application/view/LivroView.fxml");

            if (fxmlLocation == null) {
                // Mensagem de erro corrigida para refletir o caminho que estamos a tentar carregar
                System.err.println("Erro: Ficheiro FXML 'LivroView.fxml' não encontrado no classpath no caminho /application/view/");
                // Pode adicionar um diálogo de erro aqui para o utilizador, se desejar
                // Alert alert = new Alert(Alert.AlertType.ERROR, "Não foi possível carregar a interface: Ficheiro FXML não encontrado.", ButtonType.OK);
                // alert.showAndWait();
                return; // Sai do método start se o FXML não for encontrado
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            // Cria uma nova cena com o layout carregado
            Scene scene = new Scene(root);

            // Define a cena no palco (janela principal)
            primaryStage.setScene(scene);

            // Define o título da janela
            primaryStage.setTitle("Sistema de Gestão de Biblioteca - Livros"); // Título de exemplo

            // Exibe a janela
            primaryStage.show();

        } catch (Exception e) {
            // Em caso de erro ao carregar o FXML ou iniciar a aplicação
            e.printStackTrace();
            System.err.println("Erro ao iniciar a aplicação: " + e.getMessage());
            // Pode adicionar um diálogo de erro aqui para o utilizador
            // Alert alert = new Alert(Alert.AlertType.ERROR, "Não foi possível carregar a interface devido a um erro interno.", ButtonType.OK);
            // alert.showAndWait();
        }
    }

    // Método principal que inicia a aplicação JavaFX
    public static void main(String[] args) {
        launch(args); // Inicia o ciclo de vida da aplicação JavaFX
    }

    @Override
    public void stop() throws Exception {
        // Este método é chamado quando a aplicação está prestes a fechar.
        // É um bom local para fechar recursos globais, como a ligação estática da DBConnection
        // SE ESTIVESSE A USAR A VERSÃO ANTIGA DA DBConnection.
        // Com a versão MELHORADA da DBConnection, onde cada DAO gere a sua ligação,
        // fechar uma ligação estática aqui já não é relevante ou necessário,
        // pois não há uma única ligação estática persistente para fechar.
        // Mas se houver outros recursos globais que precisem de ser limpos, faria aqui.

        // Exemplo (comentado, pois não se aplica à DBConnection melhorada):
        // DBConnection.closeConnection();

        System.out.println("Aplicação a fechar.");
        super.stop();
    }
}