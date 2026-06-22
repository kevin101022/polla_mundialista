package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestiona la conexión a la base de datos.
 * Modifica URL, USER y PASSWORD según la DB que uses (SQLite, MySQL, etc.)
 */
public class DatabaseConfig {

    // ─── Ajusta estos valores según tu base de datos ───────────────────────
    private static final String URL      = "jdbc:mysql://localhost:3306/mundial_futbol"; // Conexión a MySQL
    private static final String USER     = "root"; // Cambia esto si tu usuario en XAMPP/MySQL no es root
    private static final String PASSWORD = "";     // Pon tu contraseña si le pusiste una a MySQL
    // ───────────────────────────────────────────────────────────────────────

    private static Connection connection;

    private DatabaseConfig() {}

    /**
     * Retorna la conexión activa (singleton).
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                System.err.println("Driver de MySQL no encontrado en el Classpath.");
                e.printStackTrace();
            }
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }

    /**
     * Cierra la conexión activa si existe.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para probar la conexión directamente ejecutando este archivo
    public static void main(String[] args) {
        try {
            System.out.println("Intentando conectar a MySQL...");
            getConnection();
            System.out.println("✅ ¡Conexión Exitosa a la base de datos 'mundial_futbol'!");
            closeConnection();
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar a MySQL. Revisa que XAMPP/MySQL esté encendido.");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ Error grave. Probablemente te falta el archivo Conector de MySQL (.jar).");
            e.printStackTrace();
        }
    }
}
