abstract class Usuario {
    protected int id;
    protected String nombre;
    protected boolean sancionado;
    protected double multaPendiente;

    public Usuario(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.sancionado = false;
        this.multaPendiente = 0.0;
    }

    public abstract void consultarEstado();

    public void pagarMulta() {
        if (multaPendiente > 0) {
            System.out.println("Multa pagada: $" + multaPendiente);
            multaPendiente = 0;
            sancionado = false;
        } else {
            System.out.println("No tienes multas pendientes.");
        }
    }

    public boolean puedeReservar() {
        return !sancionado;
    }
}

