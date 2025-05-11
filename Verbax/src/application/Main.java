// Classe Main que inicia a aplicação JavaFX.
// Serve como ponto de entrada para a aplicação e estende a classe Application.
package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.DBConnection; // Importa a classe DBConnection para eventual gestão de recursos ao fechar a aplicação

import java.net.URL; // Importa a classe URL para auxiliar na localização do ficheiro FXML

public class Main extends Application {

    // Método start que inicia a interface gráfica da aplicação.
    @Override
    public void start(Stage primaryStage) {
        try {
            // Tenta localizar o ficheiro FXML que define a interface gráfica.
            URL fxmlLocation = getClass().getResource("/view/LivroView.fxml");

            // Se o ficheiro FXML não for encontrado, regista a mensagem de erro e termina o método.
            if (fxmlLocation == null) {
                System.err.println("Erro: Ficheiro FXML 'LivroView.fxml' não encontrado no classpath no caminho /application/view/");
                return;
            }

            // Cria um FXMLLoader com o URL do ficheiro FXML.
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            // Carrega o layout definido no ficheiro FXML e obtém o nó raiz da interface.
            Parent root = loader.load();

            // Cria uma Scene com o nó raiz carregado.
            Scene scene = new Scene(root);

            // Define a Scene principal do Stage.
            primaryStage.setScene(scene);

            // Define o título da janela da aplicação.
            primaryStage.setTitle("Verbax - Livros");

            // Define a Scene (linha redundante, já definida anteriormente, mas preservada sem alteração sintática).
            primaryStage.setScene(scene);

            // Permite que a janela seja redimensionada.
            primaryStage.setResizable(true);

            // Inicializa a janela maximizada.
            primaryStage.setMaximized(true);

            // Mostra a janela principal da aplicação.
            primaryStage.show();

        } catch (Exception e) {
            // Regista a exceção, se ocorrer, e imprime a mensagem de erro.
            e.printStackTrace();
            System.err.println("Erro ao iniciar a aplicação: " + e.getMessage());
        }
    }

    // Método main que lança a aplicação JavaFX.
    public static void main(String[] args) {
        // Inicia o ciclo de vida da aplicação JavaFX.
        launch(args);
    }

    // Método stop que é invocado ao fechar a aplicação.
    @Override
    public void stop() throws Exception {
        // Regista no console que a aplicação está a ser encerrada.
        System.out.println("Aplicação a fechar.");
        // Chama o método stop da classe pai para finalizar a aplicação corretamente.
        super.stop();
    }
}
