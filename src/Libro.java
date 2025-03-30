class Libro implements Reservable {
    private int id;
    private String titulo;
    private String autor;
    private boolean disponible;
    private int ejemplaresDisponibles;

    public Libro(int id, String titulo, String autor, int cantidadEjemplares) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.disponible = true;
        this.ejemplaresDisponibles = cantidadEjemplares; // Inicializa la cantidad de ejemplares

    }

    public String getTitulo() {
        return titulo;
    }

    public boolean isDisponible() {
        return ejemplaresDisponibles > 0;
    }

    @Override
    public boolean reservar() {
        if (ejemplaresDisponibles > 0) {
            ejemplaresDisponibles--; // Reduce el número de ejemplares disponibles
            return true;
        }
        return false; // No se puede reservar si no hay ejemplares
    }

    @Override
    public void liberarLibro() {
        ejemplaresDisponibles++; // Aumenta el número de ejemplares disponibles
    }

    public void verDisponibilidad() {
        System.out.println(titulo + " - " + (ejemplaresDisponibles > 0 ? "Disponible (" + ejemplaresDisponibles + " ejemplares)" : "No disponible"));
    }
}
