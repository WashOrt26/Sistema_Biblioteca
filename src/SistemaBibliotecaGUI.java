import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import excepciones.*;
import java.util.List;

public class SistemaBibliotecaGUI extends JFrame {
    private Biblioteca biblioteca;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public SistemaBibliotecaGUI() {
        biblioteca = new Biblioteca();
        // Datos de prueba
        biblioteca.agregarLibro(new Libro(1, "El Principito", "Antoine de Saint-Exupéry", 3));
        biblioteca.agregarLibro(new Libro(2, "Cien Años de Soledad", "Gabriel García Márquez", 1));
        biblioteca.agregarUsuario(new Estudiante(101, "Daniel", false, 0.0));
        biblioteca.agregarUsuario(new Estudiante(202, "Maik", true, 1000.0));

        setTitle("Sistema de Biblioteca");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Panel principal con CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Panel del título
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("Sistema de Biblioteca", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Panel del menú
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(5, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JButton btnConsultar = new JButton("Consultar disponibilidad de libros");
        JButton btnReservar = new JButton("Reservar libro");
        JButton btnEstado = new JButton("Consultar estado del usuario");
        JButton btnMulta = new JButton("Pagar multa");
        JButton btnSalir = new JButton("Salir");

        // Estilo de los botones
        Font buttonFont = new Font("Arial", Font.PLAIN, 14);
        btnConsultar.setFont(buttonFont);
        btnReservar.setFont(buttonFont);
        btnEstado.setFont(buttonFont);
        btnMulta.setFont(buttonFont);
        btnSalir.setFont(buttonFont);

        menuPanel.add(btnConsultar);
        menuPanel.add(btnReservar);
        menuPanel.add(btnEstado);
        menuPanel.add(btnMulta);
        menuPanel.add(btnSalir);

        // Agregar los paneles al frame
        setLayout(new BorderLayout());
        add(titlePanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        // Agregar el menú al CardLayout
        mainPanel.add(menuPanel, "menu");
        cardLayout.show(mainPanel, "menu");

        // Action Listeners
        btnSalir.addActionListener(e -> System.exit(0));
        btnConsultar.addActionListener(e -> mostrarLibrosDisponibles());
        btnReservar.addActionListener(e -> mostrarPanelReserva());
        btnEstado.addActionListener(e -> mostrarPanelEstado());
        btnMulta.addActionListener(e -> mostrarPanelMulta());
    }

    private void mostrarLibrosDisponibles() {
        // Crear un nuevo panel para la tabla
        JPanel panelTabla = new JPanel(new BorderLayout());
        
        // Crear el modelo de la tabla
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Título");
        modelo.addColumn("Autor");
        modelo.addColumn("Ejemplares Disponibles");

        // Obtener y agregar los libros a la tabla
        List<Libro> libros = biblioteca.getLibros();
        for (Libro libro : libros) {
            Object[] fila = {
                libro.getId(),
                libro.getTitulo(),
                libro.getAutor(),
                libro.getCantidadDisponible()
            };
            modelo.addRow(fila);
        }

        // Crear la tabla
        JTable tabla = new JTable(modelo);
        tabla.setFont(new Font("Arial", Font.PLAIN, 12));
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        // Ajustar el ancho de las columnas
        tabla.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        tabla.getColumnModel().getColumn(1).setPreferredWidth(200); // Título
        tabla.getColumnModel().getColumn(2).setPreferredWidth(200); // Autor
        tabla.getColumnModel().getColumn(3).setPreferredWidth(150); // Ejemplares

        // Agregar la tabla a un JScrollPane
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        
        // Agregar título
        JLabel titulo = new JLabel("Libros Disponibles", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panelTabla.add(titulo, BorderLayout.NORTH);
        panelTabla.add(scrollPane, BorderLayout.CENTER);

        // Botón para volver al menú principal
        JButton btnVolver = new JButton("Volver al Menú");
        btnVolver.addActionListener(e -> {
            cardLayout.show(mainPanel, "menu");
        });
        panelTabla.add(btnVolver, BorderLayout.SOUTH);

        // Agregar el panel al CardLayout
        mainPanel.add(panelTabla, "libros");
        cardLayout.show(mainPanel, "libros");
    }

    private void mostrarPanelReserva() {
        // Crear panel principal para la reserva
        JPanel panelReserva = new JPanel(new BorderLayout());
        panelReserva.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel del título
        JLabel titulo = new JLabel("Reservar Libro", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panelReserva.add(titulo, BorderLayout.NORTH);

        // Panel del formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campo ID Usuario
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("ID de Usuario:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        JTextField idUsuarioField = new JTextField(20);
        formPanel.add(idUsuarioField, gbc);

        // Campo Título Libro
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Título del Libro:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        JTextField tituloLibroField = new JTextField(20);
        formPanel.add(tituloLibroField, gbc);

        // Botón de reserva
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnReservar = new JButton("Reservar");
        formPanel.add(btnReservar, gbc);

        panelReserva.add(formPanel, BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnVolver = new JButton("Volver al Menú");
        buttonPanel.add(btnVolver);

        panelReserva.add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        btnReservar.addActionListener(e -> {
            try {
                int idUsuario = Integer.parseInt(idUsuarioField.getText().trim());
                String tituloLibro = tituloLibroField.getText().trim();

                if (tituloLibro.isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "Por favor ingrese el título del libro", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Usuario usuario = biblioteca.buscarUsuarioPorId(idUsuario);
                Libro libro = biblioteca.buscarLibroPorTitulo(tituloLibro);
                biblioteca.reservarLibro(usuario, libro);

                JOptionPane.showMessageDialog(this,
                    "Reserva exitosa!\nLibro: " + libro.getTitulo() + "\nEjemplares disponibles: " + libro.getCantidadDisponible(),
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

                // Limpiar campos
                idUsuarioField.setText("");
                tituloLibroField.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Por favor ingrese un ID de usuario válido",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            } catch (UsuarioNoEncontradoException ex) {
                JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            } catch (LibroNoDisponibleException ex) {
                JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            } catch (MultaPendienteException ex) {
                JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        btnVolver.addActionListener(e -> {
            cardLayout.show(mainPanel, "menu");
        });

        // Agregar el panel al CardLayout
        mainPanel.add(panelReserva, "reserva");
        cardLayout.show(mainPanel, "reserva");
    }

    private void mostrarPanelEstado() {
        // Crear panel principal para consultar estado
        JPanel panelEstado = new JPanel(new BorderLayout());
        panelEstado.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel del título
        JLabel titulo = new JLabel("Consultar Estado del Usuario", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panelEstado.add(titulo, BorderLayout.NORTH);

        // Panel del formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campo ID Usuario
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("ID de Usuario:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        JTextField idUsuarioField = new JTextField(20);
        formPanel.add(idUsuarioField, gbc);

        // Botón de consulta
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnConsultar = new JButton("Consultar Estado");
        formPanel.add(btnConsultar, gbc);

        panelEstado.add(formPanel, BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnVolver = new JButton("Volver al Menú");
        buttonPanel.add(btnVolver);

        panelEstado.add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        btnConsultar.addActionListener(e -> {
            try {
                int idUsuario = Integer.parseInt(idUsuarioField.getText().trim());
                Usuario usuario = biblioteca.buscarUsuarioPorId(idUsuario);
                
                // Construir el mensaje de estado exactamente como en la consola
                StringBuilder estado = new StringBuilder();
                estado.append("Estado de ").append(usuario.nombre).append(":\n");
                estado.append("Sancionado: ").append(usuario.sancionado).append("\n");
                estado.append("Multa pendiente: $").append(usuario.multaPendiente);

                // Mostrar en una ventana emergente
                JOptionPane.showMessageDialog(this,
                    estado.toString(),
                    "Estado del Usuario",
                    JOptionPane.INFORMATION_MESSAGE);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Por favor ingrese un ID de usuario válido",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            } catch (UsuarioNoEncontradoException ex) {
                JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        btnVolver.addActionListener(e -> {
            cardLayout.show(mainPanel, "menu");
        });

        // Agregar el panel al CardLayout
        mainPanel.add(panelEstado, "estado");
        cardLayout.show(mainPanel, "estado");
    }

    private void mostrarPanelMulta() {
        // Crear panel principal para pagar multa
        JPanel panelMulta = new JPanel(new BorderLayout());
        panelMulta.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel del título
        JLabel titulo = new JLabel("Pagar Multa", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panelMulta.add(titulo, BorderLayout.NORTH);

        // Panel del formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campo ID Usuario
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("ID de Usuario:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        JTextField idUsuarioField = new JTextField(20);
        formPanel.add(idUsuarioField, gbc);

        // Panel para mostrar la información de la multa
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        JTextArea infoArea = new JTextArea(3, 30);
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Arial", Font.PLAIN, 14));
        infoPanel.add(new JScrollPane(infoArea), BorderLayout.CENTER);

        // Botón de consulta
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnConsultar = new JButton("Consultar Multa");
        formPanel.add(btnConsultar, gbc);

        // Botón de pago
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnPagar = new JButton("Pagar Multa");
        btnPagar.setEnabled(false);
        formPanel.add(btnPagar, gbc);

        panelMulta.add(formPanel, BorderLayout.CENTER);
        panelMulta.add(infoPanel, BorderLayout.SOUTH);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnVolver = new JButton("Volver al Menú");
        buttonPanel.add(btnVolver);

        panelMulta.add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        btnConsultar.addActionListener(e -> {
            try {
                int idUsuario = Integer.parseInt(idUsuarioField.getText().trim());
                Usuario usuario = biblioteca.buscarUsuarioPorId(idUsuario);
                
                if (usuario.sancionado) {
                    StringBuilder info = new StringBuilder();
                    info.append("Información de la Multa:\n\n");
                    info.append("El usuario tiene una multa pendiente.\n");
                    info.append("Monto de la Multa: $").append(usuario.multaPendiente).append("\n");
                    infoArea.setText(info.toString());
                    btnPagar.setEnabled(true);
                } else {
                    JOptionPane.showMessageDialog(this,
                        "El usuario no tiene multas pendientes.\nPuede realizar reservas normalmente.",
                        "Sin Multas",
                        JOptionPane.INFORMATION_MESSAGE);
                    infoArea.setText("");
                    btnPagar.setEnabled(false);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Por favor ingrese un ID de usuario válido",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            } catch (UsuarioNoEncontradoException ex) {
                JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        btnPagar.addActionListener(e -> {
            try {
                int idUsuario = Integer.parseInt(idUsuarioField.getText().trim());
                Usuario usuario = biblioteca.buscarUsuarioPorId(idUsuario);
                
                if (usuario.sancionado) {
                    usuario.pagarMulta();
                    JOptionPane.showMessageDialog(this,
                        "Multa pagada exitosamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // Limpiar y actualizar la interfaz
                    infoArea.setText("El usuario no tiene multas pendientes.\nPuede realizar reservas normalmente.");
                    btnPagar.setEnabled(false);
                    idUsuarioField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this,
                        "El usuario no tiene multas pendientes",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (UsuarioNoEncontradoException ex) {
                JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        btnVolver.addActionListener(e -> {
            cardLayout.show(mainPanel, "menu");
        });

        // Agregar el panel al CardLayout
        mainPanel.add(panelMulta, "multa");
        cardLayout.show(mainPanel, "multa");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SistemaBibliotecaGUI gui = new SistemaBibliotecaGUI();
            gui.setVisible(true);
        });
    }
} 