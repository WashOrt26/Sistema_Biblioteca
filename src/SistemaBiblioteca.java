import java.util.Scanner;
import excepciones.*;

public class SistemaBiblioteca {
    public static void main(String[] args) {
        ConexionBD.conectar();
        Scanner scanner = new Scanner(System.in);
        Biblioteca biblioteca = new Biblioteca();

        // Datos de prueba
        biblioteca.agregarLibro(new Libro(1, "El Principito", "Antoine de Saint-Exupéry",3));
        biblioteca.agregarLibro(new Libro(2, "Cien Años de Soledad", "Gabriel García Márquez",1));

        Estudiante est = new Estudiante(202, "Maik", true, 1000.0);
        biblioteca.agregarUsuario(new Estudiante(101, "Daniel", false, 0.0));
        biblioteca.agregarUsuario(est);

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

            try {
                switch (opcion) {
                    case 1:
                        biblioteca.mostrarLibrosDisponibles();
                        break;
                    case 2:
                        System.out.print("Ingrese su ID de usuario: ");
                        int idUsuario = scanner.nextInt();
                        
                        System.out.print("Ingrese el título del libro a reservar: ");
                        scanner.nextLine();
                        String titulo = scanner.nextLine();
                        
                        Usuario usuario = biblioteca.buscarUsuarioPorId(idUsuario);
                        Libro libro = biblioteca.buscarLibroPorTitulo(titulo);
                        biblioteca.reservarLibro(usuario, libro);
                        System.out.println("Reserva completada exitosamente");
                        break;
                    case 3:
                        System.out.print("Ingrese su ID de usuario: ");
                        int idEstado = scanner.nextInt();
                        Usuario user = biblioteca.buscarUsuarioPorId(idEstado);
                        user.consultarEstado();
                        break;
                    case 4:
                        System.out.print("Ingrese su ID de usuario: ");
                        int idMulta = scanner.nextInt();
                        Usuario u = biblioteca.buscarUsuarioPorId(idMulta);
                        u.pagarMulta();
                        break;
                    case 5:
                        salir = true;
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (UsuarioNoEncontradoException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (LibroNoDisponibleException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (MultaPendienteException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error inesperado: " + e.getMessage());
            }
        }

        scanner.close();
    }
}
