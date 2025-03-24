class Estudiante extends Usuario {
    public Estudiante(int id, String nombre) {
        super(id, nombre);
    }

    @Override
    public void consultarEstado() {
        System.out.println("Estado de " + nombre + ":");
        System.out.println("Sancionado: " + sancionado);
        System.out.println("Multa pendiente: $" + multaPendiente);
    }
}
