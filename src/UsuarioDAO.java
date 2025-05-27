import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public static void guardarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (id, nombre, sancionado, multaPendiente) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = ConexionBD.getConexion().prepareStatement(sql)) {
            stmt.setInt(1, usuario.id);
            stmt.setString(2, usuario.nombre);
            stmt.setBoolean(3, usuario.sancionado);
            stmt.setDouble(4, usuario.multaPendiente);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Usuario buscarUsuarioPorId(int id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";

        try (PreparedStatement stmt = ConexionBD.getConexion().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Estudiante(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getBoolean("sancionado"),
                        rs.getDouble("multaPendiente")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Usuario> obtenerTodosLosUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";

        try (Statement stmt = ConexionBD.getConexion().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                usuarios.add(new Estudiante(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getBoolean("sancionado"),
                        rs.getDouble("multaPendiente")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuarios;
    }

    public static void actualizarUsuario(Usuario usuario) {
        String sql = "UPDATE usuarios SET sancionado = ?, multaPendiente = ? WHERE id = ?";

        try (PreparedStatement stmt = ConexionBD.getConexion().prepareStatement(sql)) {
            stmt.setBoolean(1, usuario.sancionado);
            stmt.setDouble(2, usuario.multaPendiente);
            stmt.setInt(3, usuario.id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
