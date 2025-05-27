
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static Connection conexion;

    public static void conectar() {
        String url = "jdbc:mysql://localhost:3306/biblioteca"; // Ajusta el nombre de tu BD
        String usuario = "root"; // Tu usuario
        String contraseña = "Base"; // Tu contraseña

        try {
            conexion = DriverManager.getConnection(url, usuario, contraseña);
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
    }

    public static Connection getConexion() {
        return conexion;
    }
}