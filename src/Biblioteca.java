import java.util.ArrayList;
import java.util.List;
import excepciones.*;

class Biblioteca {
    private List<Libro> libros;
    private List<Usuario> usuarios;

    public Biblioteca() {
        libros = new ArrayList<>();
        usuarios = new ArrayList<>();
    }

    public void agregarLibro(Libro libro) {
        libros.add(libro);
    }

    public void agregarUsuario(Usuario usuario) {
        usuarios.add(usuario);
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
        for (Usuario usuario : usuarios) {
            if (usuario.id == id) {
                return usuario;
            }
        }
        throw new UsuarioNoEncontradoException("Usuario no encontrado con ID: " + id);
    }

    public void reservarLibro(Usuario usuario, Libro libro) throws LibroNoDisponibleException, MultaPendienteException {
        if (!usuario.puedeReservar()) {
            throw new MultaPendienteException("El usuario tiene una multa pendiente");
        }
        
        if (!libro.isDisponible()) {
            throw new LibroNoDisponibleException("El libro no está disponible");
        }

        libro.reservar();
    }
}
