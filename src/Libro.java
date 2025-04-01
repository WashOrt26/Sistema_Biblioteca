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
        this.ejemplaresDisponibles = cantidadEjemplares; 

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
        System.out.println(titulo + " - " + (ejemplaresDisponibles > 0 ? "Disponible (" + ejemplaresDisponibles + " ejemplares)" : "No disponible"));
    }
}
