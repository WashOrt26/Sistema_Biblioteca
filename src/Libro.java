class Libro implements Reservable {
    private int id;
    private String titulo;
    private String autor;
    private boolean disponible;

    public Libro(int id, String titulo, String autor) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.disponible = true;
    }

    public String getTitulo() {
        return titulo;
    }

    public boolean isDisponible() {
        return disponible;
    }

    @Override
    public boolean reservar() {
        if (disponible) {
            disponible = false;
            return true;
        }
        return false;
    }

    @Override
    public void liberarLibro() {
        disponible = true;
    }

    public void verDisponibilidad() {
        System.out.println(titulo + " - " + (disponible ? "Disponible" : "No disponible"));
    }
}
