import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO {

    // Crear una nueva reserva
    public static void crearReserva(int idUsuario, int idLibro) {
        String sql = "INSERT INTO reservas (id_usuario, id_libro) VALUES (?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idLibro);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Obtener todas las reservas de un usuario
    public static List<Libro> obtenerLibrosReservadosPorUsuario(int idUsuario) {
        List<Libro> librosReservados = new ArrayList<>();
        String sql = "SELECT l.* FROM libros l " +
                    "INNER JOIN reservas r ON l.id = r.id_libro " +
                    "WHERE r.id_usuario = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String titulo = rs.getString("titulo");
                String autor = rs.getString("autor");
                int cantidadDisponible = rs.getInt("ejemplares_disponibles");

                Libro libro = new Libro(id, titulo, autor, cantidadDisponible);
                librosReservados.add(libro);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return librosReservados;
    }

    // Eliminar una reserva (devolver libro)
    public static void eliminarReserva(int idUsuario, int idLibro) {
        String sql = "DELETE FROM reservas WHERE id_usuario = ? AND id_libro = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idLibro);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Verificar si un usuario tiene reservas
    public static boolean usuarioTieneReservas(int idUsuario) {
        String sql = "SELECT COUNT(*) FROM reservas WHERE id_usuario = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
} 