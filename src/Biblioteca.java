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
        ReservaDAO.crearReserva(usuario.id, libro.getId());
        usuario.agregarLibroReservado(libro);
        UsuarioDAO.actualizarUsuario(usuario);
    }

    public void devolverLibro(Usuario usuario, Libro libro) throws Exception {
        // Verificar que el usuario realmente tenga este libro reservado
        // Usar una comparación más robusta basada en ID
        boolean tieneLibro = false;
        for (Libro libroReservado : usuario.getLibrosReservados()) {
            if (libroReservado.getId() == libro.getId()) {
                tieneLibro = true;
                break;
            }
        }
        
        if (!tieneLibro) {
            throw new Exception("El usuario no tiene este libro reservado");
        }

        // Liberar el libro (aumentar ejemplares disponibles)
        libro.liberarLibro();
        
        // Eliminar la reserva de la base de datos
        ReservaDAO.eliminarReserva(usuario.id, libro.getId());
        
        // Quitar de la lista en memoria (usar el libro de la lista del usuario)
        for (Libro libroReservado : usuario.getLibrosReservados()) {
            if (libroReservado.getId() == libro.getId()) {
                usuario.quitarLibroReservado(libroReservado);
                break;
            }
        }
        
        // Actualizar el libro en la base de datos
        LibroDAO.actualizarEjemplares(libro.getId(), libro.getCantidadDisponible());
        
        // Actualizar el usuario en la base de datos
        UsuarioDAO.actualizarUsuario(usuario);
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
