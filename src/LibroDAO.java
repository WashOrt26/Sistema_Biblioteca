import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {

    // Insertar libro
    public static void guardarLibro(Libro libro) {
        String sql = "INSERT INTO libros (titulo, autor, ejemplares_disponibles) VALUES (?, ?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, libro.getTitulo());
            stmt.setString(2, libro.getAutor());
            stmt.setInt(3, libro.getCantidadDisponible());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Obtener todos los libros
    public static List<Libro> obtenerLibros() {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM libros";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String titulo = rs.getString("titulo");
                String autor = rs.getString("autor");
                int cantidadDisponible = rs.getInt("ejemplares_disponibles");

                Libro libro = new Libro(id, titulo, autor, cantidadDisponible);
                libros.add(libro);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return libros;
    }

    // Actualizar ejemplares disponibles
    public static void actualizarEjemplares(int idLibro, int nuevosEjemplares) {
        String sql = "UPDATE libros SET ejemplares_disponibles = ? WHERE id = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, nuevosEjemplares);
            stmt.setInt(2, idLibro);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Eliminar un libro
    public static void eliminarLibro(int idLibro) {
        String sql = "DELETE FROM libros WHERE id = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idLibro);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
