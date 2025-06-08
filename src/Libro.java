public class Libro implements Reservable {
    private int id;
    private String titulo;
    private String autor;
    private boolean disponible;
    private int ejemplaresDisponibles;

    public Libro() {} // Constructor vacío (útil para cargar desde BD)

    public Libro(int id, String titulo, String autor, int cantidadEjemplares) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.disponible = true;
        this.ejemplaresDisponibles = cantidadEjemplares;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; } // nuevo

    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }

    public int getCantidadDisponible() {
        return ejemplaresDisponibles;
    }

    public boolean isDisponible() {
        return ejemplaresDisponibles > 0;
    }

    @Override
    public boolean reservar() {
        if (ejemplaresDisponibles > 0) {
            ejemplaresDisponibles--;
            return true;
        }
        return false;
    }

    @Override
    public void liberarLibro() {
        ejemplaresDisponibles++;
    }

    public void verDisponibilidad() {
        System.out.println(titulo + " - " +
                (ejemplaresDisponibles > 0 ?
                        "Disponible (" + ejemplaresDisponibles + " ejemplares)" : "No disponible"));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Libro libro = (Libro) obj;
        return id == libro.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
