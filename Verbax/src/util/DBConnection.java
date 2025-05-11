// Classe responsável por gerir a ligação à base de dados.
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Constantes que armazenam os parâmetros de ligação à base de dados.
    private static final String URL = "jdbc:mysql://localhost:3306/verbax_db";
    private static final String USER = "root";
    private static final String PASSWORD = "admin";

    /**
     * Estabelece e retorna uma nova ligação à base de dados.
     * Cada chamada a este método cria uma nova ligação.
     *
     * @return Uma nova ligação SQL.
     * @throws SQLException Se ocorrer um erro ao obter a ligação.
     */
    public static Connection getConnection() throws SQLException {
        // Tenta carregar o driver JDBC da MySQL.
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // Em caso de não encontrar o driver, exibe uma mensagem de erro no console.
            System.err.println("Driver JDBC não encontrado: " + e.getMessage());
        }

        // Informa que se está a tentar obter uma nova conexão.
        System.out.println("Tentando obter nova conexão...");
        // Retorna uma nova instância de ligação utilizando os parâmetros definidos.
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Este método não é mais necessário no padrão onde cada DAO
     * obtém e fecha a sua própria ligação usando try-with-resources.
     * A gestão do fecho é implícita no bloco try.
     */
    public static void closeResources(AutoCloseable... resources) {
        // Percorre todos os recursos que implementam AutoCloseable.
        for (AutoCloseable resource : resources) {
            if (resource != null) {
                try {
                    // Tenta fechar cada recurso.
                    resource.close();
                } catch (Exception e) {
                    // Se ocorrer um erro ao fechar algum recurso, regista a mensagem de erro.
                    System.err.println("Erro ao fechar recurso: " + e.getMessage());
                }
            }
        }
    }
}
