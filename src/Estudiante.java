class Estudiante extends Usuario {

    public Estudiante(int id, String nombre, boolean sancionado,  double multaPendiente) {
        super(id, nombre,  sancionado, multaPendiente);
    }

    @Override
    public void consultarEstado() {
        System.out.println("Estado de " + nombre + ":");
        System.out.println("Sancionado: " + sancionado);
        System.out.println("Multa pendiente: $" + multaPendiente);
    }
}
