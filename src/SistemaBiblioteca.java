import java.util.Scanner;

public class SistemaBiblioteca {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Biblioteca biblioteca = new Biblioteca();

        // Datos de prueba
        biblioteca.agregarLibro(new Libro(1, "El Principito", "Antoine de Saint-Exupéry"));
        biblioteca.agregarLibro(new Libro(2, "Cien Años de Soledad", "Gabriel García Márquez"));

        biblioteca.agregarUsuario(new Estudiante(101, "Daniel", false, 0.0));
        biblioteca.agregarUsuario(new Estudiante(202, "Maik", true, 1000.0));

        boolean salir = false;
        while (!salir) {
            System.out.println("\n===== MENÚ BIBLIOTECA =====");
            System.out.println("1. Consultar disponibilidad de libros");
            System.out.println("2. Reservar libro");
            System.out.println("3. Consultar estado del usuario");
            System.out.println("4. Pagar multa");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    biblioteca.mostrarLibrosDisponibles();
                    break;
                case 2:
                    System.out.print("Ingrese su ID de usuario: ");
                    int idUsuario = scanner.nextInt();
                    Usuario usuario = biblioteca.buscarUsuarioPorId(idUsuario);
                    if (usuario != null) {
                        if (usuario.puedeReservar()) {
                            System.out.print("Ingrese el título del libro a reservar: ");
                            scanner.nextLine();
                            String titulo = scanner.nextLine();
                            Libro libro = biblioteca.buscarLibroPorTitulo(titulo);
                            if (libro != null && libro.isDisponible()) {
                                libro.reservar();
                                System.out.println("Reserva exitosa.");
                            } else {
                                System.out.println("Libro no disponible o no encontrado.");
                            }
                        } else {
                            System.out.println("No puedes reservar. Tienes una multa pendiente.");
                        }
                    } else {
                        System.out.println("Usuario no encontrado.");
                    }
                    break;
                case 3:
                    System.out.print("Ingrese su ID de usuario: ");
                    int idEstado = scanner.nextInt();
                    Usuario user = biblioteca.buscarUsuarioPorId(idEstado);
                    if (user != null) {
                        user.consultarEstado();
                    } else {
                        System.out.println("Usuario no encontrado.");
                    }
                    break;
                case 4:
                    System.out.print("Ingrese su ID de usuario: ");
                    int idMulta = scanner.nextInt();
                    Usuario u = biblioteca.buscarUsuarioPorId(idMulta);
                    if (u != null) {
                        u.pagarMulta();
                    } else {
                        System.out.println("Usuario no encontrado.");
                    }
                    break;
                case 5:
                    salir = true;
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }

        scanner.close();
    }
}
