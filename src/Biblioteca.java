import java.util.ArrayList;
import java.util.List;
import excepciones.*;

class Biblioteca {
    private List<Libro> libros;
    private List<Usuario> usuarios;

    public Biblioteca() {
        libros = new ArrayList<>();
    }

    public void agregarLibro(Libro libro) {
        libros.add(libro);
        LibroDAO.guardarLibro(libro);
    }

    public void agregarUsuario(Usuario usuario) {
        UsuarioDAO.guardarUsuario(usuario);
    }

    public void mostrarLibrosDisponibles() {
        System.out.println("Libros disponibles:");
        for (Libro libro : libros) {
            if (libro.isDisponible()) {
                libro.verDisponibilidad();
            }
        }
    }

    public Libro buscarLibroPorTitulo(String titulo) throws UsuarioNoEncontradoException {
        for (Libro libro : libros) {
            if (libro.getTitulo().equalsIgnoreCase(titulo)) {
                return libro;
            }
        }
        throw new UsuarioNoEncontradoException("Libro no encontrado: " + titulo);
    }

    public Usuario buscarUsuarioPorId(int id) throws UsuarioNoEncontradoException {
        Usuario usuario = UsuarioDAO.buscarUsuarioPorId(id);
        if (usuario == null) {
            throw new UsuarioNoEncontradoException("Usuario no encontrado con ID: " + id);
        }
        return usuario;
    }

    public void reservarLibro(Usuario usuario, Libro libro) throws LibroNoDisponibleException, MultaPendienteException {
        if (!usuario.puedeReservar()) {
            throw new MultaPendienteException("El usuario tiene una multa pendiente");
        }

        if (!libro.isDisponible()) {
            throw new LibroNoDisponibleException("El libro no está disponible");
        }

        libro.reservar();
        UsuarioDAO.actualizarUsuario(usuario); // si quieres guardar estado de usuario después
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void cargarLibrosDesdeBD() {
        libros = LibroDAO.obtenerLibros();
    }

    public void cargarUsuariosDesdeBD() {
        usuarios = UsuarioDAO.obtenerTodosLosUsuarios();
    }
}
