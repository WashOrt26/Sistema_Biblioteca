import java.util.ArrayList;
import java.util.List;

abstract class Usuario {
    public int id;
    public String nombre;
    public boolean sancionado;
    public double multaPendiente;
    protected List<Libro> librosReservados;

    public Usuario(int id, String nombre, boolean sancionado, double multaPendiente) {
        this.id = id;
        this.nombre = nombre;
        this.sancionado = sancionado;
        this.multaPendiente = multaPendiente;
        this.librosReservados = new ArrayList<>();
    }

    public abstract void consultarEstado();

    public void pagarMulta() {
        if (multaPendiente > 0) {
            multaPendiente = 0;
            sancionado = false;
            
            // Actualizar en la base de datos
            UsuarioDAO.actualizarUsuario(this);
        }
    }

    public boolean puedeReservar() {
        return !sancionado;
    }

    // Métodos para gestionar libros reservados
    public void agregarLibroReservado(Libro libro) {
        librosReservados.add(libro);
    }

    public void quitarLibroReservado(Libro libro) {
        librosReservados.remove(libro);
    }

    public List<Libro> getLibrosReservados() {
        return librosReservados;
    }

    public boolean tieneLibrosReservados() {
        return !librosReservados.isEmpty();
    }
}

