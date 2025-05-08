package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Configurações da ligação à base de dados
    private static final String URL = "jdbc:mysql://localhost:3306/verbax_db";
    private static final String USER = "root";
    private static final String PASSWORD = "admin"; // Coloque aqui a senha do seu MySQL, se tiver

    // A variável 'connection' estática NÃO É NECESSÁRIA para o padrão em que cada DAO fecha a sua própria ligação
    // private static Connection connection = null;

    /**
     * Estabelece e retorna uma nova ligação à base de dados.
     * Cada chamada a este método cria uma nova ligação.
     *
     * @return Uma nova ligação SQL.
     * @throws SQLException Se ocorrer um erro ao obter a ligação.
     */
    public static Connection getConnection() throws SQLException {
        // Carrega o driver JDBC. Em versões modernas do JDBC (4.0+), isto pode não ser
        // estritamente necessário se o JAR do driver estiver no classpath,
        // mas incluí-lo torna a intenção explícita.
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // Se o driver não for encontrado, é um erro de configuração crítico.
            // Podemos logar e lançar uma RuntimeException ou apenas relançar a SQLException
            // se quisermos que o erro seja tratado no nível superior.
            // Para este caso, vamos logar e continuar, mas a próxima linha que tenta
            // obter a ligação irá lançar a SQLException esperada se o driver estiver realmente em falta.
            System.err.println("Driver JDBC não encontrado: " + e.getMessage());
            // Poderia lançar: throw new RuntimeException("Driver JDBC não encontrado", e);
        }

        // Retorna uma NOVA ligação em cada chamada.
        // A gestão do fecho desta ligação fica a cargo do código que a obtém (ex: DAOs com try-with-resources).
        System.out.println("Tentando obter nova conexão..."); // Opcional para depuração
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Este método não é mais necessário no padrão onde cada DAO
     * obtém e fecha a sua própria ligação usando try-with-resources.
     * A gestão do fecho é implícita no bloco try.
     */
    // public static void closeConnection() {
    //     // Código antigo removido
    // }

    // Nota: Para fechar recursos individualmente (Statement, ResultSet),
    // ainda pode criar um método auxiliar, mas try-with-resources é preferível.
    // Este método closeResources abaixo é apenas um exemplo didático,
    // o try-with-resources nos DAOs já lida com isso.
    public static void closeResources(AutoCloseable... resources) {
        for (AutoCloseable resource : resources) {
            if (resource != null) {
                try {
                    resource.close();
                } catch (Exception e) {
                    System.err.println("Erro ao fechar recurso: " + e.getMessage());
                }
            }
        }
    }
}